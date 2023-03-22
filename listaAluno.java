import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class listaAluno extends JFrame {
    private JTable tblListarAluno;
    private JButton btnExcluir;
    private JPanel pnlListaAluno;
    private JButton btnVoltar;

    final String URL = "jdbc:mysql://localhost:3306/cadastroAluno";
    final String USER = "root";
    final String PASSWORD = "root99";
    final String EXCLUIR = "DELETE FROM aluno WHERE matricula = ?";
    final String CONSULTAR = "SELECT * FROM aluno";

    public listaAluno(){
        AddListener();
        IniciarComponentes();

    }
    public void IniciarComponentes(){
        setTitle("lista alunos");
        setSize(200, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pnlListaAluno);
        setVisible(true);
    }
    public void AddListener(){

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                telaInicial telaInicial = new telaInicial();
                telaInicial.setVisible(true);
                dispose();
            }
        });
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = null;

                try{
                    int linhaSelecionada = tblListarAluno.getSelectedRow();
                    if (linhaSelecionada < 0){
                        JOptionPane.showMessageDialog(null,"Selecione uma linha para excluir");
                        return;
                    }
                    int matricula = (int) tblListarAluno.getValueAt(linhaSelecionada, 1);

                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement stmt = connection.prepareStatement(EXCLUIR);
                    stmt.setInt(1, matricula);

                    int resultado = stmt.executeUpdate();
                    if (resultado ==1) {
                        JOptionPane.showMessageDialog(null, "registro excluido com sucesso");
                    }else{
                        JOptionPane.showMessageDialog(null,"Erro ao excluir registro!");
                    }
                    DefaultTableModel aluno = (DefaultTableModel) tblListarAluno.getModel();
                    aluno.removeRow(linhaSelecionada);
                }catch (SQLException ex) {
                    System.out.println("Erro ao excluir registro" + ex.getMessage());
                }catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Selecione uma linha para excluir");
                }

            }
        });


        DefaultTableModel alunos = new DefaultTableModel();
        alunos.addColumn("Nome");
        alunos.addColumn("Matricula");
        tblListarAluno.setModel(alunos);

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = null;
            stmt = connection.createStatement();

            ResultSet rs = null;
            rs = stmt.executeQuery(CONSULTAR);

            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getObject(1);
                row[1] = rs.getObject(2);
                alunos.addRow(row);

            }
        } catch (SQLException ex){
                throw new RuntimeException(ex);
            }


    }

    public static void main(String[] args) {
        listaAluno listaAluno = new listaAluno();
    }
}
