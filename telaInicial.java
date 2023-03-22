import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class telaInicial extends JFrame{
    private JLabel lblCadastroDeAluno;
    private JLabel lblInsiraOsDados;
    private JLabel lblNome;
    private JLabel lblMatricula;
    private JTextField txtNome;
    private JTextField txtMatricula;
    private JButton btnInserir;
    private JButton btnLimpar;
    private JButton btnVerLista;
    private JPanel pnltelaInicial;


    final String URL = "jdbc:mysql://localhost:3306/cadastroAluno";
    final String USER = "root";
    final String PASSWORD = "root99";

    final String INSERIR = "INSERT INTO aluno(nome, matricula) VALUES (?, ?)";

    public telaInicial(){
        addlistener ();
        IniciarComponentes();
        Conecta();

    }
    public void addlistener(){
        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNome.setText("");
                txtMatricula.setText("");
            }
        });

        btnVerLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listaAluno listar = new listaAluno();
                listar.setVisible(true);
                dispose();
            }
        });
    }
    public void Conecta(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado");

            final PreparedStatement stmtInserir;
            stmtInserir = connection.prepareStatement(INSERIR);

            btnInserir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nome = txtNome.getText();
                    String matriculaStr = txtMatricula.getText();

                    try {
                        int matricula = Integer.parseInt(matriculaStr);
                        stmtInserir.setString(1, nome);
                        stmtInserir.setInt(2, matricula);
                        stmtInserir.executeUpdate();
                        System.out.println("dados inseridos!");
                        JOptionPane.showMessageDialog(btnInserir, "Dados inseridos");
                        txtNome.setText("");
                        txtMatricula.setText("");
                    } catch (NumberFormatException ex) {
                        System.out.println("A matricula informada não é um numero");
                    } catch (Exception ex) {
                        System.out.println("Erro ao inserir dados no banco");
                    }


                }
            });
        }catch (Exception ex){

            System.out.println("Erro ao conectar banco de dados");

        }
    }
    public void IniciarComponentes(){
        setTitle("cadastro de alunos");
        setSize(200, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pnltelaInicial);
        setVisible(true);
    }
    public static void main(String[] args){
        telaInicial telaInicial = new telaInicial();
    }

}

