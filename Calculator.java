package test1;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;

class MyException extends Exception{
    public MyException() {
        super();
    }
    public MyException(String message) {
        super(message);
    }
}

class SwingConsole{
    public static void run(final JFrame f,final int width,final int height){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                f.setTitle(f.getClass().getSimpleName());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(width,height);
                f.setVisible(true);
            }
        });
    }
}


public class Calculator extends JFrame{

    /*
     *
     */

    private JTextField textField;				//�����ı���
    private String input;						//���
    private String operator;					//������

    public Calculator() {
        input = "";
        operator = "";

        Container container = this.getContentPane();
        JPanel panel = new JPanel();
        textField = new JTextField(30);
        textField.setEditable(false);						//�ı����ֹ�༭
        textField.setHorizontalAlignment(JTextField.LEFT);
        //textField.setBounds(100, 100, 20, 20);			//����������Ϊ���������Ч
        textField.setPreferredSize(new Dimension(400,50));
        container.add(textField, BorderLayout.NORTH);

        String[] name= {"1", "2", "3", "+", "C", "4", "5", "6",
                "-", "�˸�", "7", "8", "9", "*", "1/x", "0", "+/-", ".", "/", "="};
        panel.setLayout(new GridLayout(4,4,1,1));
        //�ڲ��������ɶ�Ӧ�İ���
        for(int i=0;i<name.length;i++) {
            JButton button = new JButton(name[i]);
            button.addActionListener(new MyActionListener());
            panel.add(button);
            button.setForeground(Color.BLACK);
            //��������ַ���ʾΪ��ɫ��������ʾΪ��ɫ�����ֺ�С����)
            if(i%5==3||i%5==4 ||i==16)
            {
                button.setForeground(Color.red);
            }
            else{
                button.setForeground(Color.BLACK);
            }
        }
        container.add(panel,BorderLayout.CENTER);

    }

    class MyActionListener implements ActionListener{			//�ڲ���ʵ�ְ�ť��Ӧ

        @Override
        public void actionPerformed(ActionEvent e) {
            int cnt=0;
            String actionCommand = e.getActionCommand();			//��ȡ��ť�ϵ��ַ���
            if(actionCommand.equals("+") || actionCommand.equals("-") || actionCommand.equals("*")
                    || actionCommand.equals("/")) {
                input += " " + actionCommand + " ";
            }
            else if(actionCommand.equals("�˸�")) {					//���ǰһ���ַ�
                input = input.substring(0,input.length()-2);
            }
            else if(actionCommand.equals("1/x")) {					//�����Ϊ1/x
                float num=Float.parseFloat(input.substring(input.length()-1));
                num=1/num;
                String temp=String.valueOf(num);
                input = input.substring(0,input.length()-2);
                input=input+" "+temp;

            }
            else if(actionCommand.equals("+/-")) {                    //����������ֱ�Ϊ���෴��
                float num = Float.parseFloat(input.substring(input.length() - 1));
                num = -1 * num;
                String temp = String.valueOf(num);
                input = input.substring(0,input.length()-2);
                input=input+" "+temp;
            }

            else if(actionCommand.equals("C")) {					//�������
                input = "";
            }
            else if(actionCommand.equals("=")) {					//���µȺ�
                try {
                    input+= "="+calculate(input);
                } catch (MyException e1) {
                    if(e1.getMessage().equals("Infinity"))
                        input+= "=" + e1.getMessage();
                    else
                        input = e1.getMessage();
                }
                textField.setText(input);
                input="";
                cnt = 1;
            }
            else
                input += actionCommand;							//��������

            if(cnt == 0)
                textField.setText(input);
        }
    }

    private String calculate(String input) throws MyException{				//���㺯��
        String[] comput = input.split(" ");
        Stack<Double> stack = new Stack<>();
        Double m = Double.parseDouble(comput[0]);
        stack.push(m);										//��һ����������ջ

        for(int i = 1; i < comput.length; i++) {
            if(i%2==1) {
                if(comput[i].equals("+"))
                    stack.push(Double.parseDouble(comput[i+1]));
                else if(comput[i].equals("-"))
                    stack.push(-Double.parseDouble(comput[i+1]));
                else if(comput[i].equals("*")) {					//��ǰһ������ջ���˷�����ջ
                    Double d = stack.peek();				//ȡջ��Ԫ��
                    stack.pop();
                    stack.push(d*Double.parseDouble(comput[i+1]));
                }
                else if(comput[i].equals("/")) {					//��ǰһ������ջ���˷�����ջ
                    double help = Double.parseDouble(comput[i+1]);
                    if(help == 0)
                        throw new MyException("Divisor cannot be zero");			//�������ִ�иú���
                    double d = stack.peek();
                    stack.pop();
                    stack.push(d/help);
                }
            }
        }

        double d = 0d;

        while(!stack.isEmpty()) {			//���
            d += stack.peek();
            stack.pop();
        }

        String result = String.valueOf(d);
        return result;
    }

    public static void main(String[] args) {
        SwingConsole.run(new Calculator(), 400, 450);
    }
}



