/**
 * 
 */
package org.pimslims.metamodel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.SystemClass;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.persistence.HibernateUtil;

/**
 * @author bl67 converted from SqlSchema.py and SqlUtil.py
 */
public abstract class MetaUtils {

    private static final Set<String> reservedSqlWords = new HashSet<String>();

    private static final Map<String, String> jdbcToPosgresqlType = new HashMap<String, String>();

    private static final Map<String, String> jdbcToHsqldbType = new HashMap<String, String>();

    private static final Map<String, String> jdbcToOracleType = new HashMap<String, String>();

    private static final Map<String, String> jdbcToMysqlType = new HashMap<String, String>();

    private static Map currentTypeMap = jdbcToPosgresqlType; //or call isOracleDB

    static {
        reservedSqlWords.add("position");
        reservedSqlWords.add("user");
        reservedSqlWords.add("procedure");
        reservedSqlWords.add("size");
        reservedSqlWords.add("date");
    }
    static {
        jdbcToPosgresqlType.put("BIT", "BOOLEAN");
        jdbcToPosgresqlType.put("INTEGER", "INT4");
        jdbcToPosgresqlType.put("BIGINT", "INT8");
        jdbcToPosgresqlType.put("FLOAT", "FLOAT8");
        jdbcToPosgresqlType.put("DOUBLE", "FLOAT8");
        jdbcToPosgresqlType.put("VARCHAR", "VARCHAR");
        jdbcToPosgresqlType.put("DATE", "DATE");
        jdbcToPosgresqlType.put("TIMESTAMP", "TIMESTAMP");
        jdbcToPosgresqlType.put("CLOB", "TEXT");
        jdbcToPosgresqlType.put("TIMESTAMPTZ", "TIMESTAMPTZ");

        jdbcToHsqldbType.put("BIT", "BIT");
        jdbcToHsqldbType.put("INTEGER", "INTEGER");
        jdbcToHsqldbType.put("BIGINT", "BIGINT");
        jdbcToHsqldbType.put("FLOAT", "FLOAT");
        jdbcToHsqldbType.put("DOUBLE", "DOUBLE");
        jdbcToHsqldbType.put("VARCHAR", "VARCHAR");
        jdbcToHsqldbType.put("DATE", "DATE");
        jdbcToHsqldbType.put("TIMESTAMP", "TIMESTAMP");
        jdbcToHsqldbType.put("CLOB", "LONGVARCHAR");

        jdbcToOracleType.put("BIT", "NUMBER (1, 0)");
        jdbcToOracleType.put("INTEGER", "NUMBER");
        jdbcToOracleType.put("BIGINT", "NUMBER");
        jdbcToOracleType.put("FLOAT", "FLOAT");
        jdbcToOracleType.put("DOUBLE", "FLOAT");
        jdbcToOracleType.put("VARCHAR", "VARCHAR2");
        jdbcToOracleType.put("DATE", "DATE");
        jdbcToOracleType.put("TIMESTAMP", "DATE");
        jdbcToOracleType.put("CLOB", "CLOB");

        jdbcToMysqlType.put("BIT", "BIT");
        jdbcToMysqlType.put("INTEGER", "INTEGER");
        jdbcToMysqlType.put("BIGINT", "BIGINT");
        jdbcToMysqlType.put("FLOAT", "FLOAT");
        jdbcToMysqlType.put("DOUBLE", "DOUBLE");
        jdbcToMysqlType.put("VARCHAR", "VARCHAR");
        jdbcToMysqlType.put("DATE", "DATETIME");
        jdbcToMysqlType.put("TIMESTAMP", "TIMESTAMP");
        jdbcToMysqlType.put("CLOB", "LONGTEXT");
    }

    /**
     * 
     */
    public MetaUtils() {
        super();

    }

    /**
     * is this attribute belong to its supper class
     * 
     * @param attribute
     * 
     */
    public static boolean isSuperAttribute(final MetaAttribute attribute) {
        if (attribute.getMetaClass().getSupertype() != null) {
            if (attribute.getMetaClass().getSupertype().getSubtypes() != null) {
                if (((MetaClassImpl) attribute.getMetaClass().getSupertype()).getAllAttributes().containsKey(
                    attribute.getName())) {
                    if (!((MetaAttributeImpl) ((MetaClassImpl) attribute.getMetaClass().getSupertype())
                        .getAllAttributes().get(attribute.getName())).isDerived()) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * 
     * @param type name in java
     * @return type name for database
     */
    public static String getDBType(String type) {
        type = type.toUpperCase();
        assert currentTypeMap.keySet().contains(type);
        return (String) currentTypeMap.get(type);
    }

    /**
     * Function that returns the table name of the super class from the sub class
     * 
     * @param metaClass
     * 
     */

    public static String getSupperClassTableName(final MetaClass metaClass) {
        final String name = ((MetaClassImpl) metaClass.getSupertype()).getDBTableName();
        return name;
    }

    /**
     * Function that returns the name of the FK constraint. It is not the name of the column.
     * 
     * @param metaClass
     * 
     */
    // result seems wrong, see MetaUtilsTest.xtestGetSubClassFkName()
    public static String getSubClassFkName(final MetaClass metaClass) {
        final String name =
            apiNameToSqlName(getPackageShortName(metaClass) + "_" + apiShortName(metaClass.getShortName())
                + "_" + apiShortName(getSuperclassName(metaClass)));

        testNameLen(name);
        return name;
    }

    /**
     * Function that returns the name of the superclass
     * 
     * @param metaClass
     * 
     */
    public static String getSuperclassName(final MetaClass metaClass) {
        final String superTypeShortName = ((MetaClassImpl) metaClass).getSupertype().getShortName();

        return superTypeShortName;
    }

    /**
     * Function that returns the reference table name of 'this' role where the foreign key points in
     * many-to-many case
     * 
     * @param role
     * 
     */
    public static String getThisRoleTableName(final MetaRole role) {
        return getRoleTableName(role);
    }

    /**
     * Function that returns the name of the FK constraint for "this" role in many-to-many table It is not the
     * name of the column.
     * 
     * @param mtoMTableName
     * 
     * @param role
     * @return name of the FK constraint for "this" role
     */
    public static String getThisRoleFkName(final MetaRole role) {
        String name = getRoleFkName(role);
        if (role.getOtherRole() != null) {
            if (role.getRoleName().equalsIgnoreCase(role.getOtherRole().getRoleName())) {
                name = name + "1";
            }
        }

        testNameLen(name);
        return name;

    }

    /**
     * Function that returns the reference table name of the other role where the foreign key points in
     * many-to-many case
     * 
     * @param role
     * @return the reference table name of the other role
     */
    public static String getOtherRoleTableName(final MetaRole role) {
        String name;
        name = getClassTableName(role.getOwnMetaClass());
        return name;
    }

    /**
     * Function that returns the name of the FK constraint for other role in many-to-many table It is not the
     * name of the column.
     * 
     * @param mtoMTableName
     * 
     * @param role
     * 
     */
    public static String getOtherRoleFkName(String mtoMTableName, final MetaRole role) {
        assert isManyToManyRole(role);
        String name;
        final MetaRole otherRole = role.getOtherRole();
        if (otherRole != null) {
            name = getRoleFkName(otherRole);
            if (role.getRoleName().equalsIgnoreCase(role.getOtherRole().getRoleName())) {
                name = name + "2";
            }
        } else {
            name = mtoMTableName + "_" + "OtherRole";
        }

        testNameLen(name);
        return name;

    }

    /**
     * Function that returns the FK column name for "other" role in many-to-many table
     * 
     * @param role
     * @return returns the FK column name
     */
    public static String getOtherRoleColName(final MetaRole role) {
        assert isManyToManyRole(role);
        final javax.persistence.JoinTable joinTable = ((MetaRoleImpl) role).getDBJoinTable();
        if (joinTable == null) {
            return null;
        }
        final JoinColumn[] joinColumns = joinTable.joinColumns();
        return joinColumns[0].name();

    }

    /**
     * Function that returns the FK column name for "this" role in many-to-many table
     * 
     * @param role
     * @return returns the FK column name
     */
    public static String getThisRoleColName(final MetaRole role) {
        assert isManyToManyRole(role);
        final javax.persistence.JoinTable joinTable = ((MetaRoleImpl) role).getDBJoinTable();
        if (joinTable == null) {
            return null;
        }
        final JoinColumn[] inverseJoinColumns = joinTable.inverseJoinColumns();
        return inverseJoinColumns[0].name();
    }

    /**
     * @param role
     * @return
     */
    private static boolean isManyToManyRole(final MetaRole role) {
        if (role.getHigh() != 1) {
            //for one way m-m
            if (role.getOtherRole() == null) {
                if (((MetaRoleImpl) role).getDBJoinTable() != null) {
                    return true;
                }
            }
            //for two way
            else if (role.getOtherRole().getHigh() != 1) {
                return true;
            }
        }
        return false;
    }

    public static String getSingleName(String name) {

        if (name.toLowerCase().endsWith("species")) {
            return name;
        } else if (name.toLowerCase().endsWith("aliases")) {
            return "alias";
        } else if (name.toLowerCase().endsWith("addresses")) {
            return "address";
        } else if (name.endsWith("ies")) {
            name = name.substring(0, name.length() - 3) + "y";
        } else if (name.endsWith("s")) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    /**
     * Function that returns the many-to-may table name
     * 
     * @param role
     * @return the many-to-may table name
     */
    public static String getMToMTableName(final MetaRole role) {
        assert isManyToManyRole(role);
        final javax.persistence.JoinTable joinTable = ((MetaRoleImpl) role).getDBJoinTable();
        if (joinTable == null) {
            return null;
        }
        return joinTable.name();
    }

    /**
     * Function that returns the reference table name where the foreign key points (whether or not hicard = 1
     * or hicard != 1)
     * 
     */
    public static String getRoleTableName(final MetaRole role) {
        String name;
        if (role.getRoleName().equalsIgnoreCase("ApplicationData")
            && role.getOtherMetaClass().getShortName().equalsIgnoreCase("NormalStored")) {
            name = "IMPL_MEMOPSBASECLASS";
        } else {
            name = getClassTableName(role.getOtherMetaClass());
        }
        return name;
    }

    /**
     * Function that returns the name of the FK constraint. It is not the name of the column.
     * 
     * @param role
     * @return name of the FK constraint
     */
    public static String getRoleFkName(final MetaRole role) {
        final String name =
            apiNameToSqlName(getPackageShortName(role.getOwnMetaClass()) + "_"
                + apiShortName(role.getOwnMetaClass().getShortName()) + "_"
                + apiShortName(role.getRoleName()));

        testNameLen(name);
        return name;
    }

    /**
     * Function that returns the FK column name from that role in the role.clazz table if role.hicard = 1
     * 
     * @param role
     * @return the FK column name
     */
    public static String getRoleColName(final MetaRole role) {
        final JoinColumn joinColumn = ((MetaRoleImpl) role).getDBJoinColumn();
        if (joinColumn == null) {
            return null;
        }
        return joinColumn.name();
    }

    public static String qualifiedNameofRole(final MetaRole role) {
        String qName;
        qName = role.getOwnMetaClass().getMetaClassName() + "." + role.getRoleName();
        return qName;
    }

    /**
     * 
     * @param role
     * @return Determines whether this role is a many-to-one link or a one-to-one
     */
    public static boolean isManyToOne_or_OneToOneRole(final MetaRole role) {
        if (role.getHigh() == 1) {
            return true;
        }
        //else
        return false;
        /*
         * simplified from: if( role.hicard == 1 and ((other is None) or (other.hicard != 1) or (other.hicard ==
         * 1)):) return True; else: return false;
         */
    }

    /**
     * 
     * @param role
     * @return Determines whether this role is a one-to-many link.
     */
    public static boolean isOneToManyRole(final MetaRole role) {
        final MetaRole other = role.getOtherRole();
        if (other == null) {
            return false;
        } else if (role.getHigh() != 1 && other.getHigh() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param role
     * @return Determines whether this role should be ignored.
     */
    public static boolean isIgnoredRole(final MetaRole role) {
        if (role == null) {
            return true;
        } else if (((MetaRoleImpl) role).isAbstract()) {
            return true;
        } else if (((MetaRoleImpl) role).isDerived()) {
            return true;
        }

        /*
         * If we have core tables and all packages, this ignore will ignore role link to core table's class //
         * ignore roles related with ignored packeages (defined in ModelImpl.getPackages()) else
         * if(!CCPNMetaUtils.isRelevantPakage(role.getOtherMetaClass().getMetaClassName())) return true;
         */
        /*
         * the way to get roles from class is changed from ccpnMetaClass.roleAllNames to
         * ccpnMetaClass.roleNames // if it is a role belong to its abstract class, then ingore it else
         * if(((CCPNMetaClass)role.getOwnMetaClass()).getCCPNSupertype()!=null) {
         * if(((CCPNMetaClass)role.getOwnMetaClass()).getCCPNSupertype().getSubtypes()!=null)
         * if(((CCPNMetaClass)role.getOwnMetaClass()).getCCPNSupertype().getClass()==MetaClass.class)
         * if(((CCPNMetaClass)((CCPNMetaClass)role.getOwnMetaClass()).getCCPNSupertype()).getAllMetaRoles()!=null)
         * if(( (CCPNMetaClass)(((CCPNMetaClass)role.getOwnMetaClass()).getCCPNSupertype()
         * )).getAllMetaRoles().containsKey(role.getRoleName())) return true; }
         */
        return false;
    }

    /**
     * @param name
     * @return short name (< 10 chars) from a name. Changes ChemComp -> ChemComp Changes NonStdChemComp ->
     *         NonStChCo Changes ChemAtomSetSysName -> ChemAtSeSyNa Changes ResidueProb -> ResiPr
     */
    static String apiShortName(final String name) {
        String shortName;
        if (name.length() > 10) {
            shortName = "";
            int counter = 0;
            int firstPart = 1;
            for (int i = 0; i < name.length(); i++) {
                final char c = name.charAt(i);
                if (Character.isUpperCase(c) && !(counter == 0)) {
                    firstPart = 0;
                }
                if (Character.isUpperCase(c) || counter == 0) {
                    counter = 1;
                    shortName = shortName + c;
                } else {
                    counter = counter + 1;
                    if (firstPart == 1) {
                        if (counter <= 4) {
                            shortName = shortName + c;
                        }
                    } else {
                        if (counter <= 2) {
                            shortName = shortName + c;
                        }
                    }
                }
            }
        } else {
            shortName = name;
        }

        // assert(shortName.length()<=10);

        return shortName;
    }

    /**
     * e.g. IMPL_USERGROUP for memops.Implementation.UserGroup
     * 
     * @param metaClass which have the pachage path
     * 
     */
    public static String getClassTableName(final MetaClass metaClass) {
        final String name = ((MetaClassImpl) metaClass).getDBTableName();
        return name;

    }

    /**
     * Make column name for simple attributes e.g. NAME
     * 
     * @param attribute
     * 
     */
    public static String getColumnName(final MetaAttribute attribute) {
        final Column dbColumn = ((MetaAttributeImpl) attribute).getDBColumn();
        if (dbColumn == null) {
            return null;
        }
        return dbColumn.name();
    }

    /**
     * Make table name for multi-value attributes e.g. PEOP_ORGA_ADDRESSES
     * 
     * @param metaClass
     * @param attributeName
     * 
     */
    public static String getMultiAttributeTableName(final MetaClass metaClass, final String attributeName) {

        return getPackageShortName(metaClass) + "_" + MetaUtils.apiShortName(metaClass.getShortName()) + "_"
            + MetaUtils.apiShortName(attributeName);
    }

    public static Class getTypeFromGenericType(final Type genericType) {
        if (!genericType.toString().contains("List") && !genericType.toString().contains("Collection")) {
            return null; // TODO can that be right?
        }
        if (genericType.toString().contains("java.lang.String")) {
            return java.lang.String.class;
        } else if (genericType.toString().contains("java.lang.Integer")) {
            return java.lang.Integer.class;
        } else if (genericType.toString().contains("java.lang.Long")) {
            return java.lang.Long.class;
        } else if (genericType.toString().contains("java.sql.Timestamp")) {
            return java.sql.Timestamp.class;
        } else if (genericType.toString().contains("java.sql.Boolean")) {
            return java.lang.Boolean.class;
        } else if (genericType.toString().contains("java.sql.Float")) {
            return java.lang.Float.class;
        }

        throw new RuntimeException("Unknown type:" + genericType);
    }

    /**
     * Convert the java type to sql type
     * 
     * @param c class
     * @param length length of attribute
     * 
     */
    public static String getSqlType(final MetaAttribute ma) {
        Class c = ma.getType();
        if (java.util.Collection.class.isAssignableFrom(c)) {
            c = getTypeFromGenericType(((MetaAttributeImpl) ma).getGenericType());
        }
        final int length = ma.getLength();
        if (c == java.lang.Integer.class || c == java.lang.Integer[].class) {
            return getDBType("Integer");
        } else if (c == java.lang.String.class || c == java.lang.String[].class) {
            if (length < 0 || length > 10485760) {
                return getDBType("CLOB");
            } else if (length == 0) {
                //TODO the meta info of multiple attribute is not available yet
                return getDBType("varchar") + "(" + 80 + ")"; //default length for string
            } else {
                return getDBType("varchar") + "(" + length + ")";
            }
        } else if (c == java.lang.Float.class || c == java.lang.Float[].class) {
            return (getDBType("Float"));
        } else if (c == Double.class || c == Double[].class) {
            return (getDBType("Double"));
        } else if (c == java.lang.Boolean.class) {
            return (getDBType("bit"));
        } else if (c == java.sql.Timestamp.class) {
            return (getDBType("timestamp"));
        } else if (c == java.lang.Long.class) {
            return (getDBType("BIGINT"));
        } else if (c == java.util.Calendar.class) {
            return (getDBType("timestamptz")); // should this be TIMESTAMPTZ ?
        }
        throw new RuntimeException("Unknown type:" + c + " for attribute: " + ma.getName());
    }

    public static boolean columnTypeCheck(final MetaAttribute attribute, final String columnType) {
        if (HibernateUtil.isOracleDB()) {
            return true;
        }
        if (MetaUtils.getSqlType(attribute).contains(columnType.toUpperCase())) {
            return true;
        } else if (MetaUtils.getSqlType(attribute).equalsIgnoreCase("Text")
            && columnType.equalsIgnoreCase("varchar")) {
            return true;
        } else {
            throw new RuntimeException(attribute.getMetaClass() + "'s attribute" + attribute.getName()
                + "column) type changed from " + columnType.toUpperCase() + " to "
                + MetaUtils.getSqlType(attribute));
        }
    }

    /**
     * 
     * @param c
     * 
     */
    public static boolean isMultiAttribute(final Class c) {
        if (c.getName().startsWith("[")) {
            return true;
        }
        if (c.getName().toLowerCase().contains("list")) {
            return true;
        }
        if (c.getName().toLowerCase().contains("collection")) {
            return true;
        }
        return false;

    }

    /**
     * @param metaClass the class name include package pass
     * @return the short name of package
     */
    public static String getPackageShortName(final MetaClass metaClass) {
        return ((MetaClassImpl) metaClass).getPackageShortName();
    }

    /**
     * 
     * @param fullClassName the class name include package pass
     * @return one word name of class
     * 
     *         public static String getClassName(String fullClassName) { String[] names =
     *         fullClassName.split("\\."); int last = names.length - 1; return names[last]; }
     */

    /**
     * to see if the length of name is too long What does this matter?
     * 
     * @param name
     */
    static void testNameLen(final String name) {
        /* if (name.length() > 30) {
            System.out.println("WARNING: name " + "'" + name + "'" + " length = " + name.length());
        } */
    }

    /**
     * Function that returns sql name from a python name. Changes package_anExampleString51 to
     * PACKAGE_ANEXAMPLESTRING51
     * 
     * @param name
     */
    static String apiNameToSqlName(final String name) {

        if (reservedSqlWords.contains(name)) {
            return name + "_";
        }
        return name;
    }

    /**
     * @param string
     * @return a string like the string passed, but with the initial letter in lower case and remove Hb from
     *         string bigining eg: HbConformings ->conformings
     */
    public static String getStandardName(String hbName) {
        if (hbName.startsWith("Hb")) {
            hbName = hbName.substring(2);
        }
        final char initial = hbName.charAt(0);
        return Character.toLowerCase(initial) + hbName.substring(1);
    }

    /**
     * MetaUtils.getSuperClassKeyName how does this differ from getSubclassId?
     * 
     * @param metaClass
     * @return
     */
    public static String getKeyName(final MetaClass metaClass) {
        if (!(metaClass.getSupertype() instanceof MetaClassImpl)) {
            return "dbid";
        }
        final MetaClassImpl type = (MetaClassImpl) metaClass;
        final Class clazz = type.getJavaClass();
        if (LabBookEntry.class.equals(clazz) || SystemClass.class.equals(clazz)
            || PublicEntry.class.equals(clazz) || Attachment.class.equals(clazz)) {
            return "dbid";
        }
        final String name = getSuperclassName(metaClass) + "Id";
        return name;
    }

}
