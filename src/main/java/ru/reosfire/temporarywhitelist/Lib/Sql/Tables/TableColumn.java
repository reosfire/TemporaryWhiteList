package ru.reosfire.temporarywhitelist.Lib.Sql.Tables;

public class TableColumn
{
    private String name;
    private ColumnType type;
    private ColumnFlag[] flags;

    public TableColumn(String name, ColumnType type, ColumnFlag... flags)
    {
        this.name = name;
        this.type = type;
        this.flags = flags;
    }

    @Override
    public String toString()
    {
        String result = "`" + name + "` " + type.ToString();
        for (ColumnFlag flag : flags)
        {
            result += " " + flag.toString();
        }
        return result;
    }
}