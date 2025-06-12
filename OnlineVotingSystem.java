import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnlineVotingSystem {

    private JFrame frame;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField voterIDField;
    private JTextField ageField;
    private JRadioButton partyA;
    private JRadioButton partyB;
    private JRadioButton partyC;
    private ButtonGroup group;
    private JButton submitButton;
    private JButton resultsButton;
    
    private int partyAVotes = 0;
    private int partyBVotes = 0;
    private int partyCVotes = 0;

    public OnlineVotingSystem() {
        frame = new JFrame("Online Voting System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GroupLayout(frame.getContentPane()));
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);

        JLabel nameLabel = new JLabel("Enter Name:");
        nameField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Enter Phone:");
        phoneField = new JTextField(20);
        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new PhoneNumberFilter());

        JLabel voterIDLabel = new JLabel("Enter Voter ID:");
        voterIDField = new JTextField(20);
        ((AbstractDocument) voterIDField.getDocument()).setDocumentFilter(new NumberOnlyFilter());

        JLabel ageLabel = new JLabel("Enter Age:");
        ageField = new JTextField(20);
        ((AbstractDocument) ageField.getDocument()).setDocumentFilter(new NumberOnlyFilter());

        JLabel voteLabel = new JLabel("Cast Your Vote Here:");
        partyA = new JRadioButton("Party A");
        partyB = new JRadioButton("Party B");
        partyC = new JRadioButton("Party C");

        group = new ButtonGroup();
        group.add(partyA);
        group.add(partyB);
        group.add(partyC);

        submitButton = new JButton("Submit Your Vote");
        resultsButton = new JButton("Check Results");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitVote();
            }
        });

        resultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkResults();
            }
        });

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(phoneLabel)
                    .addComponent(voterIDLabel)
                    .addComponent(ageLabel)
                    .addComponent(voteLabel)
                    .addComponent(partyA)
                    .addComponent(partyB)
                    .addComponent(partyC)
                    .addComponent(submitButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameField)
                    .addComponent(phoneField)
                    .addComponent(voterIDField)
                    .addComponent(ageField)
                    .addComponent(resultsButton))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(voterIDLabel)
                    .addComponent(voterIDField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(ageLabel)
                    .addComponent(ageField))
                .addComponent(voteLabel)
                .addComponent(partyA)
                .addComponent(partyB)
                .addComponent(partyC)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton)
                    .addComponent(resultsButton))
        );

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void submitVote() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String voterID = voterIDField.getText();
        String ageText = ageField.getText();
        String vote = null;

        if (partyA.isSelected()) {
            vote = "Party A";
        } else if (partyB.isSelected()) {
            vote = "Party B";
        } else if (partyC.isSelected()) {
            vote = "Party C";
        }

        if (name.isEmpty() || phone.isEmpty() || voterID.isEmpty() || ageText.isEmpty() || vote == null) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields and select a party.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (phone.length() != 10) {
            JOptionPane.showMessageDialog(frame, "Phone number must be 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int age = Integer.parseInt(ageText);
            if (age < 18) {
                JOptionPane.showMessageDialog(frame, "You must be at least 18 years old to vote.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (vote.equals("Party A")) {
                    partyAVotes++;
                } else if (vote.equals("Party B")) {
                    partyBVotes++;
                } else if (vote.equals("Party C")) {
                    partyCVotes++;
                }
                JOptionPane.showMessageDialog(frame, "Vote submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        voterIDField.setText("");
        ageField.setText("");
        group.clearSelection();
    }

    private void checkResults() {
        String winner;
        if (partyAVotes > partyBVotes && partyAVotes > partyCVotes) {
            winner = "Party A";
        } else if (partyBVotes > partyAVotes && partyBVotes > partyCVotes) {
            winner = "Party B";
        } else if (partyCVotes > partyAVotes && partyCVotes > partyBVotes) {
            winner = "Party C";
        } else {
            winner = "No clear winner (it's a tie or no votes)";
        }
        
        String results = "Results:\n" +
                         "Party A: " + partyAVotes + " votes\n" +
                         "Party B: " + partyBVotes + " votes\n" +
                         "Party C: " + partyCVotes + " votes\n" +
                         "Winner: " + winner;
        JOptionPane.showMessageDialog(frame, results, "Voting Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private class NumberOnlyFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[0-9]+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[0-9]+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }

    private class PhoneNumberFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[0-9]+") && (fb.getDocument().getLength() + string.length() <= 10)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[0-9]+") && (fb.getDocument().getLength() + text.length() - length <= 10)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }

    public static void main(String[] args) {
        new OnlineVotingSystem();
    }
}