import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class app extends JFrame {
    private JTextField nomeCliente;
    private JSpinner dataPedido;
    private JRadioButton[] paoOptions;
    private JRadioButton[] frioOptions;
    private JRadioButton[] friosOptions;
    private JCheckBox[] frutaOptions;
    private JRadioButton[] cookiesOptions;
    private JButton btnEnviar;
    private ButtonGroup groupPao, groupFrio, groupFrios, groupCookies;
    
    private static final String SHEET_URL = "https://api.sheetmonkey.io/form/fn1n3fA5kQYTtb5GqRexyY";
    private static final int MAX_FRUTAS = 2;

    public app() {
        setTitle("☕ Monte seu Café");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(244, 241, 234));
        mainPanel.setLayout(new FlowLayout());
        
        // Painel de formulário
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setMaximumSize(new Dimension(500, 900));
        
        // Título
        JLabel titulo = new JLabel("☕ Monte seu Café");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(111, 78, 55));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titulo);
        formPanel.add(Box.createVerticalStrut(20));
        
        // --- DADOS ---
        formPanel.add(criarFieldset("Seus Dados", criarPainelDados()));
        formPanel.add(Box.createVerticalStrut(15));
        
        // --- PÃO ---
        String[] paos = {"Pão Francês", "Pão de Queijo", "Pão Integral", "Sovado"};
        String[] paosValues = {"frances", "queijo", "integral", "sovado"};
        paoOptions = new JRadioButton[paos.length];
        groupPao = new ButtonGroup();
        formPanel.add(criarFieldset("Escolha 1 tipo de Pão", 
            criarPainelRadio(paos, paoOptions, groupPao, paosValues)));
        formPanel.add(Box.createVerticalStrut(15));
        
        // --- EMBUTIDO/FRIO ---
        String[] frios = {"Presunto", "Peito Defumado", "Salame Colonial"};
        String[] friosValues = {"presunto", "peito defumado", "salame colonial"};
        frioOptions = new JRadioButton[frios.length];
        groupFrio = new ButtonGroup();
        formPanel.add(criarFieldset("Escolha 1 Embutido/Frio", 
            criarPainelRadio(frios, frioOptions, groupFrio, friosValues)));
        formPanel.add(Box.createVerticalStrut(15));
        
        // --- QUEIJOS ---
        String[] queijos = {"Queijo Prato", "Queijo Mussarela", "Queijo Colonial"};
        String[] queijosValues = {"queijo prato", "Queijo mussarela", "queijo colonial"};
        friosOptions = new JRadioButton[queijos.length];
        groupFrios = new ButtonGroup();
        formPanel.add(criarFieldset("Escolha 1 Embutido/Queijo", 
            criarPainelRadio(queijos, friosOptions, groupFrios, queijosValues)));
        formPanel.add(Box.createVerticalStrut(15));
        
        // --- FRUTAS ---
        String[] frutas = {"Mamão", "Banana", "Melancia", "Uva", "Maçã"};
        String[] frutasValues = {"mamao", "banana", "melancia", "uva", "maca"};
        frutaOptions = new JCheckBox[frutas.length];
        formPanel.add(criarFieldset("Escolha até 2 Frutas", 
            criarPainelCheckBox(frutas, frutaOptions, frutasValues)));
        formPanel.add(Box.createVerticalStrut(15));
        
        // --- COOKIES ---
        String[] cookies = {"Tradicional", "Red Velvet", "Nutella", "Leite Ninho", "Amendoin"};
        String[] cookiesValues = {"tradicional", "red velvet", "nutella", "leite ninho", "amedoin"};
        cookiesOptions = new JRadioButton[cookies.length];
        groupCookies = new ButtonGroup();
        formPanel.add(criarFieldset("Escolha até 1 sabor de Cookies", 
            criarPainelRadio(cookies, cookiesOptions, groupCookies, cookiesValues)));
        formPanel.add(Box.createVerticalStrut(20));
        
        // --- BOTÃO ENVIAR ---
        btnEnviar = new JButton("Fazer Pedido");
        btnEnviar.setBackground(new Color(111, 78, 55));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnviar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEnviar.addActionListener(e -> enviarPedido());
        formPanel.add(btnEnviar);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane);
        
        add(mainPanel);
    }

    private JPanel criarPainelDados() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Nome
        JLabel labelNome = new JLabel("Nome Completo:");
        labelNome.setFont(new Font("Arial", Font.PLAIN, 12));
        nomeCliente = new JTextField(20);
        nomeCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(labelNome);
        panel.add(nomeCliente);
        panel.add(Box.createVerticalStrut(10));
        
        // Data
        JLabel labelData = new JLabel("Data do Pedido:");
        labelData.setFont(new Font("Arial", Font.PLAIN, 12));
        dataPedido = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dataPedido, "dd/MM/yyyy");
        dataPedido.setEditor(editor);
        dataPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(labelData);
        panel.add(dataPedido);
        
        return panel;
    }

    private JPanel criarPainelRadio(String[] labels, JRadioButton[] buttons, 
                                    ButtonGroup group, String[] values) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = new JRadioButton(labels[i]);
            buttons[i].setActionCommand(values[i]);
            buttons[i].setOpaque(false);
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }
        
        return panel;
    }

    private JPanel criarPainelCheckBox(String[] labels, JCheckBox[] boxes, String[] values) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        for (int i = 0; i < labels.length; i++) {
            boxes[i] = new JCheckBox(labels[i]);
            boxes[i].setActionCommand(values[i]);
            boxes[i].setOpaque(false);
            boxes[i].addItemListener(e -> validarFrutas());
            panel.add(boxes[i]);
        }
        
        return panel;
    }

    private JPanel criarFieldset(String titulo, JPanel conteudo) {
        JPanel fieldset = new JPanel();
        fieldset.setLayout(new BorderLayout());
        fieldset.setBorder(new TitledBorder(titulo));
        fieldset.setOpaque(false);
        fieldset.add(conteudo, BorderLayout.CENTER);
        return fieldset;
    }

    private void validarFrutas() {
        int selecionadas = 0;
        for (JCheckBox fruta : frutaOptions) {
            if (fruta.isSelected()) {
                selecionadas++;
            }
        }
        
        if (selecionadas >= MAX_FRUTAS) {
            for (JCheckBox fruta : frutaOptions) {
                if (!fruta.isSelected()) {
                    fruta.setEnabled(false);
                }
            }
        } else {
            for (JCheckBox fruta : frutaOptions) {
                fruta.setEnabled(true);
            }
        }
    }

    private void enviarPedido() {
        // Validação
        if (nomeCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite seu nome!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (groupPao.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, escolha um tipo de pão!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (groupFrio.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, escolha um embutido/frio!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (groupFrios.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, escolha um queijo!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mostrar resumo do pedido para confirmação
        if (!mostrarConfirmacaoPedido()) {
            return;
        }
        
        // Desabilitar botão durante envio
        btnEnviar.setEnabled(false);
        btnEnviar.setText("Enviando...");
        
        // Enviar em thread separada
        new Thread(() -> {
            try {
                Map<String, String> dados = new HashMap<>();
                dados.put("nome", nomeCliente.getText());
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dados.put("data", sdf.format((Date) dataPedido.getValue()));
                
                if (groupPao.getSelection() != null) {
                    dados.put("pao", groupPao.getSelection().getActionCommand());
                }
                if (groupFrio.getSelection() != null) {
                    dados.put("frio", groupFrio.getSelection().getActionCommand());
                }
                if (groupFrios.getSelection() != null) {
                    dados.put("frios", groupFrios.getSelection().getActionCommand());
                }
                
                String frutas = Arrays.stream(frutaOptions)
                    .filter(JCheckBox::isSelected)
                    .map(AbstractButton::getActionCommand)
                    .collect(Collectors.joining(","));
                if (!frutas.isEmpty()) {
                    dados.put("fruta", frutas);
                }
                
                if (groupCookies.getSelection() != null) {
                    dados.put("cookies", groupCookies.getSelection().getActionCommand());
                }
                
                enviarDados(dados);
                
                SwingUtilities.invokeLater(() -> {
                    mostrarTelaSucesso();
                    
                    // Limpar formulário
                    nomeCliente.setText("");
                    dataPedido.setValue(new Date());
                    groupPao.clearSelection();
                    groupFrio.clearSelection();
                    groupFrios.clearSelection();
                    groupCookies.clearSelection();
                    for (JCheckBox fruta : frutaOptions) {
                        fruta.setSelected(false);
                        fruta.setEnabled(true);
                    }
                    
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("Fazer Pedido");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, 
                        "Erro! Verifique a conexão ou o link. " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("Fazer Pedido");
                });
            }
        }).start();
    }

    private boolean mostrarConfirmacaoPedido() {
        boolean[] confirmado = {false};
        
        JDialog confirmDialog = new JDialog(this, "Confirmação do Pedido", true);
        confirmDialog.setSize(500, 600);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        confirmDialog.setResizable(false);
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);
        
        // Título
        JLabel titulo = new JLabel("=== RESUMO DO PEDIDO ===");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(20));
        
        // Conteúdo do resumo
        StringBuilder resumo = new StringBuilder();
        resumo.append("Nome: ").append(nomeCliente.getText()).append("\n");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        resumo.append("Data: ").append(sdf.format((Date) dataPedido.getValue())).append("\n\n");
        
        resumo.append("Pão: ");
        if (groupPao.getSelection() != null) {
            resumo.append(getNomeOpcao(groupPao.getSelection().getActionCommand(), "pao"));
        }
        resumo.append("\n");
        
        resumo.append("Embutido/Frio: ");
        if (groupFrio.getSelection() != null) {
            resumo.append(getNomeOpcao(groupFrio.getSelection().getActionCommand(), "frio"));
        }
        resumo.append("\n");
        
        resumo.append("Queijo: ");
        if (groupFrios.getSelection() != null) {
            resumo.append(getNomeOpcao(groupFrios.getSelection().getActionCommand(), "frios"));
        }
        resumo.append("\n\n");
        
        resumo.append("Frutas: ");
        boolean temFruta = false;
        for (JCheckBox fruta : frutaOptions) {
            if (fruta.isSelected()) {
                if (temFruta) resumo.append(", ");
                resumo.append(fruta.getText());
                temFruta = true;
            }
        }
        if (!temFruta) resumo.append("Nenhuma");
        resumo.append("\n\n");
        
        resumo.append("Cookies: ");
        if (groupCookies.getSelection() != null) {
            resumo.append(getNomeOpcao(groupCookies.getSelection().getActionCommand(), "cookies"));
        } else {
            resumo.append("Nenhum");
        }
        
        JTextArea textoResumo = new JTextArea(resumo.toString());
        textoResumo.setFont(new Font("Arial", Font.PLAIN, 13));
        textoResumo.setEditable(false);
        textoResumo.setOpaque(false);
        textoResumo.setLineWrap(false);
        painel.add(textoResumo);
        
        painel.add(Box.createVerticalStrut(20));
        
        JLabel pergunta = new JLabel("Deseja confirmar este pedido?");
        pergunta.setFont(new Font("Arial", Font.BOLD, 13));
        pergunta.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(pergunta);
        
        painel.add(Box.createVerticalStrut(20));
        
        // Botões
        JPanel botoesPainel = new JPanel();
        botoesPainel.setLayout(new BoxLayout(botoesPainel, BoxLayout.X_AXIS));
        botoesPainel.setOpaque(false);
        
        JButton btnSim = new JButton("Sim, Confirmar");
        btnSim.setBackground(new Color(34, 139, 34));
        btnSim.setForeground(Color.WHITE);
        btnSim.setFont(new Font("Arial", Font.BOLD, 12));
        btnSim.addActionListener(e -> {
            confirmado[0] = true;
            confirmDialog.dispose();
        });
        
        JButton btnNao = new JButton("Não, Cancelar");
        btnNao.setBackground(new Color(220, 20, 60));
        btnNao.setForeground(Color.WHITE);
        btnNao.setFont(new Font("Arial", Font.BOLD, 12));
        btnNao.addActionListener(e -> {
            confirmado[0] = false;
            confirmDialog.dispose();
        });
        
        botoesPainel.add(btnSim);
        botoesPainel.add(Box.createHorizontalStrut(10));
        botoesPainel.add(btnNao);
        
        painel.add(botoesPainel);
        
        JScrollPane scrollPane = new JScrollPane(painel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        confirmDialog.add(scrollPane);
        confirmDialog.setVisible(true);
        
        return confirmado[0];
    }

    private void mostrarTelaSucesso() {
        JDialog sucessoDialog = new JDialog(this, "Pedido Finalizado", true);
        sucessoDialog.setSize(400, 300);
        sucessoDialog.setLocationRelativeTo(this);
        sucessoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(30, 30, 30, 30));
        painel.setBackground(new Color(144, 238, 144));
        
        JLabel icone = new JLabel("✓");
        icone.setFont(new Font("Arial", Font.BOLD, 60));
        icone.setForeground(new Color(34, 139, 34));
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(icone);
        
        painel.add(Box.createVerticalStrut(20));
        
        JLabel titulo = new JLabel("Pedido Finalizado!");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(titulo);
        
        painel.add(Box.createVerticalStrut(20));
        
        JLabel mensagem = new JLabel("<html>Sucesso, " + nomeCliente.getText() + "!<br>" +
            "Seu pedido já está na nossa planilha.</html>");
        mensagem.setFont(new Font("Arial", Font.PLAIN, 14));
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(mensagem);
        
        painel.add(Box.createVerticalStrut(30));
        
        JButton btnOk = new JButton("OK");
        btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOk.addActionListener(e -> sucessoDialog.dispose());
        painel.add(btnOk);
        
        sucessoDialog.add(painel);
        sucessoDialog.setVisible(true);
    }

    private String getNomeOpcao(String valor, String tipo) {
        if (tipo.equals("pao")) {
            if (valor.equals("frances")) return "Pão Francês";
            if (valor.equals("queijo")) return "Pão de Queijo";
            if (valor.equals("integral")) return "Pão Integral";
            if (valor.equals("sovado")) return "Sovado";
        } else if (tipo.equals("frio")) {
            if (valor.equals("presunto")) return "Presunto";
            if (valor.equals("peito defumado")) return "Peito Defumado";
            if (valor.equals("salame colonial")) return "Salame Colonial";
        } else if (tipo.equals("frios")) {
            if (valor.equals("queijo prato")) return "Queijo Prato";
            if (valor.equals("Queijo mussarela")) return "Queijo Mussarela";
            if (valor.equals("queijo colonial")) return "Queijo Colonial";
        } else if (tipo.equals("cookies")) {
            if (valor.equals("tradicional")) return "Tradicional";
            if (valor.equals("red velvet")) return "Red Velvet";
            if (valor.equals("nutella")) return "Nutella";
            if (valor.equals("leite ninho")) return "Leite Ninho";
            if (valor.equals("amedoin")) return "Amendoim";
        }
        return valor;
    }

    private void enviarDados(Map<String, String> dados) throws Exception {
        StringBuilder urlParams = new StringBuilder();
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            if (urlParams.length() > 0) {
                urlParams.append("&");
            }
            urlParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        
        URL url = new URL(SHEET_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        byte[] postData = urlParams.toString().getBytes("UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData);
            os.flush();
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            throw new Exception("Erro HTTP: " + responseCode);
        }
        
        conn.disconnect();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            app frame = new app();
            frame.setVisible(true);
            
        });
    }
}
