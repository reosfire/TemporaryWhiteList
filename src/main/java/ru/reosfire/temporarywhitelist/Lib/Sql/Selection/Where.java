package ru.reosfire.temporarywhitelist.Lib.Sql.Selection;

public class Where implements ISelectionAttribute
{
    private final StringBuilder result = new StringBuilder("WHERE ");

    public Where(String var, Comparer comparer, String value)
    {
        result.append(var).append(comparer.toString()).append("'").append(value).append("'");
    }

    public Where(String var, Comparer comparer, long value)
    {
        result.append(var).append(comparer.toString()).append(value);
    }

    public Where(String var, Comparer comparer, boolean value)
    {
        result.append(var).append(comparer.toString()).append(value ? 1 : 0);
    }

    @Override
    public String toSqlString()
    {
        return result.toString();
    }
}