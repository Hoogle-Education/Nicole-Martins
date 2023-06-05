import dao.DaoFactory;
import dao.ProductsDao;
import dao.UserDao;
import models.Product;
import models.User;

import javax.swing.*;

import java.awt.*;

public class Main extends JFrame {
   private UserDao userDao;
   private ProductsDao productsDao;
   private JTable usersTable;
   private JTable productsTable;

   public Main(UserDao userDao, ProductsDao productsDao) {
      this.userDao = userDao;
      this.productsDao = productsDao;

      setTitle("CRUD de Usuários e Produtos");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setPreferredSize(new Dimension(800, 600));

      JPanel mainPanel = new JPanel(new BorderLayout());
      JPanel usersPanel = createUserPanel();
      JPanel productsPanel = createProductsPanel();

      mainPanel.add(usersPanel, BorderLayout.WEST);
      mainPanel.add(productsPanel, BorderLayout.EAST);

      getContentPane().add(mainPanel);
      pack();
   }

   private JPanel createUserPanel() {
      JPanel panel = new JPanel(new BorderLayout());

      JButton addUserButton = new JButton("Adicionar Usuário");
      JButton editUserButton = new JButton("Editar Usuário");
      JButton deleteUserButton = new JButton("Deletar Usuário");
      JButton addProductButton = new JButton("Adicionar Produto");

      // Configuração dos botões
      addUserButton.addActionListener(e -> {
         // Criação dos campos de entrada de dados
         JTextField userNameField = new JTextField();
         JTextField userUsernameField = new JTextField();
         JTextField userEmailField = new JTextField();
         JTextField userPasswordField = new JTextField();

         // Criação do painel para os campos de entrada de dados
         JPanel inputPanel = new JPanel(new GridLayout(4, 2));
         inputPanel.add(new JLabel("Nome do usuário:"));
         inputPanel.add(userNameField);
         inputPanel.add(new JLabel("Username do usuário:"));
         inputPanel.add(userUsernameField);
         inputPanel.add(new JLabel("Email do usuário:"));
         inputPanel.add(userUsernameField);
         inputPanel.add(new JLabel("Senha do usuário:"));
         inputPanel.add(userUsernameField);

         int result = JOptionPane.showConfirmDialog(null, inputPanel, "Adicionar Usuário", JOptionPane.OK_CANCEL_OPTION);
         if (result == JOptionPane.OK_OPTION) {
            try {
               String userName = userNameField.getText();
               String userUsername = userUsernameField.getText();
               String userEmail = userEmailField.getText();
               String userPassword = userPasswordField.getText();


               User user = new User(userName, userUsername, userEmail, userPassword);
               userDao.save(user);

               // TODO Atualiza a tabela de produtos
               // TODO Implemente o método que atualiza a tabela productsTable com os dados atualizados
               JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Erro: Insira valores válidos Usuário.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
         }
      });

      editUserButton.addActionListener(e -> {
         // Criação dos campos de entrada de dados
         JTextField userNameField = new JTextField();
         JTextField userUsernameField = new JTextField();
         JTextField userEmailField = new JTextField();

         // Criação do painel para os campos de entrada de dados
         JPanel inputPanel = new JPanel(new GridLayout(3, 2));
         inputPanel.add(new JLabel("Editar Nome do usuário:"));
         inputPanel.add(userNameField);
         inputPanel.add(new JLabel("Editar Username do usuário:"));
         inputPanel.add(userUsernameField);
         inputPanel.add(new JLabel("EditarEmail do usuário:"));
         inputPanel.add(userUsernameField);


         int result = JOptionPane.showConfirmDialog(null, inputPanel, "Editar Usuário", JOptionPane.OK_CANCEL_OPTION);
         if (result == JOptionPane.OK_OPTION) {
            try {
               String userName = userNameField.getText();
               String userUsername = userUsernameField.getText();
               String userEmail = userEmailField.getText();

               // TODO check password
               User user = new User(userName, userUsername, userEmail, "");
               userDao.update(user);

               // TODO Atualiza a tabela de produtos
               // TODO Implemente o método que atualiza a tabela productsTable com os dados atualizados
               JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso!");
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Erro: Insira valores válidos Usuário.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
         }
      });

      deleteUserButton.addActionListener(e -> {
         // Lógica para deletar um usuário
      });

      addProductButton.addActionListener(e -> {
         // Lógica para adicionar um produto a um usuário
      });

      // Adiciona os componentes no painel
      panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
      panel.add(addUserButton, BorderLayout.NORTH);
      panel.add(editUserButton, BorderLayout.NORTH);
      panel.add(deleteUserButton, BorderLayout.NORTH);
      panel.add(addProductButton, BorderLayout.SOUTH);

      return panel;
   }

   private JPanel createProductsPanel() {
      JPanel panel = new JPanel(new BorderLayout());

      JButton addProductButton = new JButton("Adicionar Produto");
      JButton editProductButton = new JButton("Editar Produto");
      JButton deleteProductButton = new JButton("Deletar Produto");

      // Configuração dos botões
      addProductButton.addActionListener(e -> {
         System.out.println("tentando adicionar confirma");
         // Criação dos campos de entrada de dados
         JTextField productNameField = new JTextField();
         JTextField productPriceField = new JTextField();

         // Criação do painel para os campos de entrada de dados
         JPanel inputPanel = new JPanel(new GridLayout(2, 2));
         inputPanel.add(new JLabel("Nome do Produto:"));
         inputPanel.add(productNameField);
         inputPanel.add(new JLabel("Preço do Produto:"));
         inputPanel.add(productPriceField);

         int result = JOptionPane.showConfirmDialog(null, inputPanel, "Adicionar Produto", JOptionPane.OK_CANCEL_OPTION);
         if (result == JOptionPane.OK_OPTION) {
            try {
               String productName = productNameField.getText();
               double productPrice = Double.parseDouble(productPriceField.getText());

               // Criação do objeto Product
               Product newProduct = new Product(productName, productPrice);

               // Lógica para salvar o novo produto no banco de dados
               productsDao.save(newProduct);

               // TODO Atualiza a tabela de produtos
               // TODO método que atualiza a tabela productsTable com os dados atualizados

               JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso!");
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Erro: Insira valores válidos para o ID e Preço do Produto.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
         }
      });


      editProductButton.addActionListener(e -> {
         // Lógica para editar um produto
      });

      deleteProductButton.addActionListener(e -> {
         // Lógica para deletar um produto
      });

      // Adiciona os componentes no painel
      panel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
      panel.add(addProductButton, BorderLayout.NORTH);
      panel.add(editProductButton, BorderLayout.NORTH);
      panel.add(deleteProductButton, BorderLayout.NORTH);

      return panel;
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         Main mainFrame = new Main(DaoFactory.getUserDao(), DaoFactory.getProductDao());
         mainFrame.setVisible(true);
      });
   }
}
