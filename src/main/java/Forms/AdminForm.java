package Forms;

import Database.DatabaseManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminForm extends JFrame {
    private JPanel AdminForm;
    private JButton deleteUser;
    private JButton checkUser;
    private JTextField userName;
    private JButton addUser;
    private JFrame frame = null;

    AdminForm(final JFrame previousFrame) {
        super("Admin form");
        frame = this;
        setContentPane(AdminForm);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                previousFrame.pack();
                previousFrame.setVisible(true);
                frame.dispose();
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        checkUser.addActionListener(e -> {
            System.out.println("\"" + userName.getText() + "\"");
            if (userName.getText().equals("dropDatabase")) {
                DatabaseManager.dropDatabase();
                DatabaseManager.initDatabase();
                return;
            }
            if (userName.getText().equals("") || userName.getText() == null) {
                JOptionPane.showMessageDialog(AdminForm.this.getParent(), "Поле имени пользователя должно быть заполнено!", "Проверка пользователя", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int userRights;
            if ((userRights = DatabaseManager.checkUser(userName.getText())) != -1) {
                StringBuilder infoString = new StringBuilder("Имя пользователя: ");
                infoString.append(userName.getText()).append("\nТип пользователя: ");
                if (userRights == 1) {
                    infoString.append("Администратор");
                } else {
                    infoString.append("Пользователь");
                }
                JOptionPane.showMessageDialog(AdminForm.this.getParent(), infoString.toString(), "Проверка пользователя", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(AdminForm.this.getParent(), "Ошибка! Пользователь не найден.", "Проверка пользователя", JOptionPane.ERROR_MESSAGE);

            }
        });
        addUser.addActionListener(e -> {
            System.out.println("\"" + userName.getText() + "\"");
            if (userName.getText().equals("") || userName.getText() == null) {
                JOptionPane.showMessageDialog(getParent(), "Поле имени пользователя должно быть заполнено!", "Добавление пользователя", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String password = null;
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Задайте пароль для нового пользователя:");
            JPasswordField pass = new JPasswordField(10);
            panel.add(label);
            panel.add(pass);
            String[] options = new String[]{"OK", "Cancel"};
            int option = JOptionPane.showOptionDialog(getParent(), panel, "Пароль",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[1]);
            if (option == 0) // pressing OK button
            {
                password = new String(pass.getPassword());
            }
            if (password == null || password.equals("")) {
                JOptionPane.showMessageDialog(getParent(), "Ошибка! Поле пароля должно быть заполнено!", "Добавление пользователя", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("\"" + password + "\"");
                if (DatabaseManager.addUser(userName.getText(), password)) {
                    JOptionPane.showMessageDialog(getParent(), "Пользователь добавлен", "Добавление пользователя", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Ошибка! Пользователь с данным именем уже зарегистрирован в системе!", "Добавление пользователя!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteUser.addActionListener(e -> {
            System.out.println("\"" + userName.getText() + "\"");
            if (userName.getText().equals("") || userName.getText() == null) {
                JOptionPane.showMessageDialog(getParent(), "Ошибка! Поле имени пользователя должно быть заполнено!", "Удаление пользователя", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int result = DatabaseManager.deleteUser(userName.getText());
            if (result == 0) {
                JOptionPane.showMessageDialog(getParent(), "Пользователь успешно удален.", "Удаление пользователя", JOptionPane.INFORMATION_MESSAGE);
            } else if (result == 1) {
                JOptionPane.showMessageDialog(getParent(), "Ошибка! Невозможно удалить администратора.", "Удаление пользователя", JOptionPane.ERROR_MESSAGE);
            } else if (result == 2) {
                JOptionPane.showMessageDialog(getParent(), "Ошибка! Пользователь не существует.", "Удаление пользователя", JOptionPane.ERROR_MESSAGE);
            } else if (result == 3) {
                JOptionPane.showMessageDialog(getParent(), "Ошибка! Ошибка доступа к БД.", "Удаление пользователя", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        AdminForm = new JPanel();
        AdminForm.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        AdminForm.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Имя пользователя:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userName = new JTextField();
        panel1.add(userName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        AdminForm.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkUser = new JButton();
        checkUser.setText("Проверить пользователя");
        panel2.add(checkUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteUser = new JButton();
        deleteUser.setText("Удалить пользователя");
        panel2.add(deleteUser, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addUser = new JButton();
        addUser.setText("Добавить пользователя");
        panel2.add(addUser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return AdminForm;
    }
}
