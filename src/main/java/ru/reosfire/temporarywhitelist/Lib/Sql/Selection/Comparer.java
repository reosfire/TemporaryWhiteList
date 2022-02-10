package ru.reosfire.temporarywhitelist.Lib.Sql.Selection;

public enum Comparer
{
    Equal, NotEqual, GreaterThan, LessThan, GreaterThanOrEquals, LessThanOrEquals, Between, Like, In;

    @Override
    public String toString()
    {
        switch (this)
        {
            case Equal:
                return "=";
            case NotEqual:
                return "<>";
            case GreaterThan:
                return ">";
            case LessThan:
                return "<";
            case GreaterThanOrEquals:
                return ">=";
            case LessThanOrEquals:
                return "<=";
            case Between:
                return "BETWEEN";
            case Like:
                return "LIKE";
            case In:
                return "IN";
            default:
                return this.name();
        }
    }
}