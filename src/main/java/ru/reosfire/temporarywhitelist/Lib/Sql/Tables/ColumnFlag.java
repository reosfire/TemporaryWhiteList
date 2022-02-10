package ru.reosfire.temporarywhitelist.Lib.Sql.Tables;

public enum ColumnFlag implements IColumnAttribute
{
    Not_null, Unique;

    @Override
    public String toString()
    {
        return this.name().toUpperCase().replace('_', ' ');
    }

    @Override
    public String ToString()
    {
        return toString();
    }
}