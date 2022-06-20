package me.prism3.loggervelocity.utils.config;

import java.util.*;

public final class Configuration {

    private static final char SEPARATOR = '.';
    final Map<String, Object> self;
    private final Configuration defaults;

    public Configuration(Configuration defaults)
    {
        this( new LinkedHashMap<>(), defaults );
    }

    Configuration(Map<?, ?> map, Configuration defaults) {
        this.self = new LinkedHashMap<>();
        this.defaults = defaults;

        for ( Map.Entry<?, ?> entry : map.entrySet() )
        {
            String key = ( entry.getKey() == null ) ? "null" : entry.getKey().toString();

            if ( entry.getValue() instanceof Map )
            {
                this.self.put( key, new Configuration( (Map) entry.getValue(), ( defaults == null ) ? null : defaults.getSection( key ) ) );
            } else
            {
                this.self.put( key, entry.getValue() );
            }
        }
    }

    private Configuration getSectionFor(String path) {
        int index = path.indexOf( SEPARATOR );
        if ( index == -1 )
        {
            return this;
        }

        String root = path.substring( 0, index );
        Object section = self.get( root );
        if ( section == null )
        {
            section = new Configuration( ( defaults == null ) ? null : defaults.getSection( root ) );
            self.put( root, section );
        }

        return (Configuration) section;
    }

    private String getChild(String path)
    {
        int index = path.indexOf( SEPARATOR );
        return ( index == -1 ) ? path : path.substring( index + 1 );
    }

    /*------------------------------------------------------------------------*/
    @SuppressWarnings("unchecked")
    public <T> T get(String path, T def)
    {
        Configuration section = getSectionFor( path );
        Object val;
        if ( section == this )
        {
            val = self.get( path );
        } else
        {
            val = section.get( getChild( path ), def );
        }

        if ( val == null && def instanceof Configuration )
        {
            self.put( path, def );
        }

        return ( val != null ) ? (T) val : def;
    }

    public Object get(String path)
    {
        return get( path, getDefault( path ) );
    }

    public Object getDefault(String path)
    {
        return ( defaults == null ) ? null : defaults.get( path );
    }

    /*------------------------------------------------------------------------*/
    public Configuration getSection(String path)
    {
        Object def = getDefault( path );
        return (Configuration) get( path, ( def instanceof Configuration ) ? def : new Configuration( ( defaults == null ) ? null : defaults.getSection( path ) ) );
    }

    public int getInt(String path)
    {
        Object def = getDefault( path );
        return getInt( path, ( def instanceof Number ) ? ( (Number) def ).intValue() : 0 );
    }

    public int getInt(String path, int def)
    {
        Number val = get( path, def );
        return (val != null) ? val.intValue() : def;
    }

    public long getLong(String path)
    {
        Object def = getDefault( path );
        return getLong( path, ( def instanceof Number ) ? ( (Number) def ).longValue() : 0 );
    }

    public long getLong(String path, long def)
    {
        Number val = get( path, def );
        return (val != null) ? val.longValue() : def;
    }

    public boolean getBoolean(String path)
    {
        Object def = getDefault( path );
        return getBoolean( path, ( def instanceof Boolean ) && (Boolean) def );
    }

    public boolean getBoolean(String path, boolean def)
    {
        Boolean val = get( path, def );
        return (val != null) ? val : def;
    }

    public String getString(String path)
    {
        Object def = getDefault( path );
        return getString( path, ( def instanceof String ) ? (String) def : "" );
    }

    public String getString(String path, String def)
    {
        String val = get( path, def );
        return (val != null) ? val : def;
    }

    public List<String> getStringList(String path)
    {
        List<?> list = getList( path );
        List<String> result = new ArrayList<>();

        for ( Object object : list )
        {
            if ( object instanceof String )
            {
                result.add( (String) object );
            }
        }

        return result;
    }

    /*------------------------------------------------------------------------*/
    public List<?> getList(String path)
    {
        Object def = getDefault( path );
        return getList( path, ( def instanceof List<?> ) ? (List<?>) def : Collections.emptyList() );
    }

    public List<?> getList(String path, List<?> def)
    {
        List<?> val = get( path, def );
        return (val != null) ? val : def;
    }
}