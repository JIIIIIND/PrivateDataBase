package exercise;

enum Type
{
    STRING, DOUBLE, INT, CHAR
}
public class Field
{
    private Type _type;
    private String _name;
    private int _size;

    public Field()
    {
        _type = null;
        _name = null;
        _size = 0;
    }

    public Type getType()
    {
        return _type;
    }
    public void setType(Type value)
    {
        _type = value;
    }

    public String getName()
    {
        return _name;
    }
    public void setName(String value)
    {
        _name = value;
    }

    public int getSize()
    {
        return _size;
    }
    public void setSize(int value)
    {
        _size = value;
    }
}
