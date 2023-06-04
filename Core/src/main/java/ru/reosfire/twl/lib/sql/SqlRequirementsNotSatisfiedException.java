package ru.reosfire.twl.lib.sql;

public class SqlRequirementsNotSatisfiedException extends Exception
{
    public SqlRequirementsNotSatisfiedException()
    {
    }

    public SqlRequirementsNotSatisfiedException(String message)
    {
        super(message);
    }

    public SqlRequirementsNotSatisfiedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SqlRequirementsNotSatisfiedException(Throwable cause)
    {
        super(cause);
    }

    public SqlRequirementsNotSatisfiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}