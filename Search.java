import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Search {

    private ActionListener InsertValue;
    private static Search instance;
    private static Record _record;

    private Search() { }

    public static void getInstance()
    {
        if(instance == null)
            instance = new Search();
        _record = Record.getInstance();
        instance.RecordSearch();
    }
    //입력받은 키 값과 같은 값을 포함하는 레코드를 테이블에 나타냄
    private void RecordSearch()
    {
        _record.getMakeForm("Enter Key","Key");
        _record.set_currentRec(_record.get_first());
        _record.set_index(DataBase.get_selectField());

        InsertValue = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String key = _record.getTextField1().getText();

                if (_record.getFind(key, _record.get_index()) != null)
                {
                    DataBase.get_form().setModel();
                }
                else
                {
                    DataBaseForm.ShowMessage("해당 값이 없습니다.");
                }
                _record.getTextField1().setText("");
                _record.getEnterButton().removeActionListener(InsertValue);
                _record.getFrame().dispose();
            }   //End actionPerformed
        };
        _record.getEnterButton().addActionListener(InsertValue);
        DataBase.set_selectField(-1);
    }
}
