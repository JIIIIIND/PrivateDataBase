import java.io.*;

public class DataBase
{
    private static int _selectField;
    //Table에서 선택한 필드의 인덱스 저장

    private static int _fieldCount;
    //필드의 개수 저장

    private static int _sortField;
    //정렬의 기준이 되는 필드 선택

    private static Field[] _fields;

    private static TreeManager _manager;
    private static Record _record;
    private static DataBaseForm _form;

    public static void main(String[] args) throws Exception
    {
        _record = Record.getInstance();
        _form = new DataBaseForm();
        _manager = TreeManager.getInstance();
    }

    //def파일 : 1. 필드의 수 2. sort 필드, 3번째부터 3개씩 각각의 필드 내용
    private static void Load(String fileName)
    {
        String dataFile = fileName.concat(".dat");
        String defFile = fileName.concat(".def");
        File file = new File(defFile);

        try
        {
            //한줄에 하나의 정보가 있기때문에 다른것 보다는 BufferedReader가 좋다고 판단
            BufferedReader br = new BufferedReader(new FileReader(file));
            _record.set_Init(TreeManager.getInstance().getRoot());
            //def의 처음 내용은 필드의 수
            int count = Integer.parseInt(br.readLine());
            _fieldCount = count;
            //두번째 내용은 sort 필드
            _sortField = Integer.parseInt(br.readLine());
            int i = 0;

            _fields = new Field[_fieldCount];

            //필드의 수 만큼 반복하며 필드 정보를 넣어준다.
            while(i < count)
            {
                _fields[i] = new Field();

                String line = br.readLine();
                _fields[i].setName(line);

                line = br.readLine();
                line = line.toLowerCase();

                switch (line.charAt(0))
                {
                    case 's':
                        _fields[i].setType(Type.STRING);
                        break;
                    case 'd':
                        _fields[i].setType(Type.DOUBLE);
                        break;
                    case 'i':
                        _fields[i].setType(Type.INT);
                        break;
                    case 'c':
                        _fields[i].setType(Type.CHAR);
                        break;
                    default:
                        return;
                }

                try
                {
                    int size = Integer.parseInt(br.readLine());
                    _fields[i].setSize(size);
                    i++;
                }
                catch(NumberFormatException e)
                {
                    DataBaseForm.ShowMessage("너무 큰 값이 입력되어 있습니다.");
                }

            }//필드 정보 입력 완료

            file = new File(dataFile);
            br = new BufferedReader(new FileReader(file));
            boolean isLast = false;
            //레코드 입력 시작
            while(!isLast)
            {
                TreeNode t = new TreeNode();
                for(int index = 0; index < _fieldCount; index++) {
                    String line = br.readLine();
                    if((line != null) && (line.length() > _fields[index].getSize()))
                    {
                        DataBaseForm.ShowMessage("사이즈보다 큰 값이 입력되어 있습니다.");
                        _record.set_Init(TreeManager.getInstance().getRoot());
                        set_fields(null);
                        return;
                    }
                    try
                    {
                        //data파일의 마지막에 도달하면 반복문 종료 플래그 설정
                        if (line == null)
                        {
                            isLast = true;
                            break;
                        }
                        if (_fields[index].getType() == Type.STRING)
                        {
                            t.set_data(line);
                        }
                        else if (_fields[index].getType() == Type.CHAR)
                        {
                            t.set_data(line.charAt(0));
                        }
                        else if (_fields[index].getType() == Type.INT)
                        {
                            int value = Integer.parseInt(line);
                            t.set_data(value);
                        }
                        else
                        {
                            double value = Double.parseDouble(line);
                            t.set_data(value);
                        }
                    }
                    catch (NumberFormatException error)
                    {
                        DataBaseForm.ShowMessage("지나치게 큰 값이 입력되어 있습니다.");
                        return;
                    }
                }
                if(!isLast) _record.addNode(t);
            }
            _record.set_currentRec(_record.getRoot());
            _form.setModel();
        }
        catch(IOException e) {
            DataBaseForm.ShowMessage("지정된 파일을 찾을 수 없습니다.");
        }
    }

    //필드 정보를 정의 한 파일 하나, 레코드 정의 파일 하나 생성
    private static void Save(String fileName)
    {
        TreeNode temp = _record.getRoot();

        String dataFile = fileName.concat(".dat");
        String defFile = fileName.concat(".def");
        File file = new File(dataFile);
        try
        {
            PrintWriter pw = new PrintWriter(file);
            //DataBase.SaveTreeData(pw, _record.getRoot());
            _manager.Save(pw, _record.getRoot());
            pw.close();

            file = new File(defFile);
            pw = new PrintWriter(file);

            pw.println(_fieldCount);
            pw.println(_sortField);

            //필드 정보를 파일로 입력
            for(int i = 0; i < DataBase._fieldCount; i ++)
            {
                pw.println(_fields[i].getName());
                pw.println(_fields[i].getType());
                pw.println(_fields[i].getSize());
            }

            pw.close();
        }
        catch(IOException e) { }
    }

    //필드안에 내용이 다 들었는지 확인
    public static boolean IsFullField()
    {
        if(_fields != null)
        {
            for(int i = 0; i < DataBase.get_fields().length; i++ )
            {
                if(DataBase.get_fields()[i] == null)
                {
                    return false;
                }
            }
            return true;
        }
        else
            return false;
    }

    //여기서부터는 접근자, 설정자
    public static void getSave(String fileName) { Save(fileName); }
    public static void getLoad(String fileName) { Load(fileName); }

    public static Record get_record() { return _record; }

    public static DataBaseForm get_form() {return _form;}

    public static int get_selectField() {return _selectField; }
    public static void set_selectField(int value) {_selectField = value; }

    public static int get_fieldCount() {return _fieldCount; }
    public static void set_fieldCount(int value) {_fieldCount = value; }

    public static int get_sortField() {return _sortField; }
    public static void set_sortField(int value) {_sortField = value; }

    public static Field[] get_fields() {return _fields; }
    public static void set_fields(Field[] fields) {_fields = fields; }

}
