package ru.reosfire.temporarywhitelist.Lib.Sql.Tables;

public class TableColumn
{
    private final String name;
    private final ColumnType type;
    private final ColumnFlag[] flags;

    public TableColumn(String name, ColumnType type, ColumnFlag... flags)
    {
        this.name = name;
        this.type = type;
        this.flags = flags;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("`" + name + "` " + type.toSqlString());
        for (ColumnFlag flag : flags)
        {
            result.append(" ").append(flag.toString());
        }
        return result.toString();
    }
}