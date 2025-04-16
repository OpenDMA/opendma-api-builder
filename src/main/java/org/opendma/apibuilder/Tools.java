package org.opendma.apibuilder;

public class Tools
{
    
    public static String upperCaseFirstChar(String s)
    {
        if(s.length() > 1)
        {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        else
        {
            return s;
        }
    }
    
    public static String lowerCaseFirstChar(String s)
    {
        if(s.length() > 1)
        {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        }
        else
        {
            return s;
        }
    }
    
    public static String toSnakeCase(String s)
    {
        String result = "";
        for(int i = 0; i < s.length(); i++)
        {
            char ch = s.charAt(i);
            if(Character.isUpperCase(ch))
            {
                if(i != 0)
                {
                    result = result + "_";
                }
                result = result + Character.toLowerCase(ch);                
            }
            else
            {
                result = result + ch;
            }
        }
        return result;
    }

    private Tools()
    {
        // prevent instantiation
    }
    
}
