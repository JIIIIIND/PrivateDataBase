import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveLoad
{
    private JPanel panel1;
    private JTextField value;
    private JButton enterButton;
    private JLabel JLabel;

    private ActionListener SaveFile;
    private ActionListener LoadFile;

    public SaveLoad(boolean save)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.add(panel1);
        frame.setSize(400, 300);
        frame.setVisible(true);

        //save가 true면 저장 기능 활성화/false면 로드 기능 활성화
        if(save)
        {
            SaveFile = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(!value.getText().isEmpty())
                    {
                        DataBase.getSave(value.getText());
                    }
                    else
                        DataBaseForm.ShowMessage("파일 이름을 입력하세요");
                    enterButton.removeActionListener(SaveFile);
                    frame.dispose();
                }
            };
            enterButton.addActionListener(SaveFile);
        }
        else
        {
            LoadFile = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(!value.getText().isEmpty())
                    {
                        DataBase.getLoad(value.getText());
                    }
                    else
                        DataBaseForm.ShowMessage("파일 이름을 입력하세요");
                    enterButton.removeActionListener(LoadFile);
                    frame.dispose();
                }
            };
            enterButton.addActionListener(LoadFile);
        }
    }
}
