/**
 * Autogenerated by Thrift Compiler (0.15.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.mdc.mim.user;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.15.0)", date = "2024-07-06")
public class IdentifyReq implements org.apache.thrift.TBase<IdentifyReq, IdentifyReq._Fields>, java.io.Serializable, Cloneable, Comparable<IdentifyReq> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("IdentifyReq");

  private static final org.apache.thrift.protocol.TField USER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("UserName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PASSWD_MD5_FIELD_DESC = new org.apache.thrift.protocol.TField("PasswdMd5", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new IdentifyReqStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new IdentifyReqTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String UserName; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String PasswdMd5; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    USER_NAME((short)1, "UserName"),
    PASSWD_MD5((short)2, "PasswdMd5");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // USER_NAME
          return USER_NAME;
        case 2: // PASSWD_MD5
          return PASSWD_MD5;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.USER_NAME, new org.apache.thrift.meta_data.FieldMetaData("UserName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PASSWD_MD5, new org.apache.thrift.meta_data.FieldMetaData("PasswdMd5", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(IdentifyReq.class, metaDataMap);
  }

  public IdentifyReq() {
  }

  public IdentifyReq(
    java.lang.String UserName,
    java.lang.String PasswdMd5)
  {
    this();
    this.UserName = UserName;
    this.PasswdMd5 = PasswdMd5;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public IdentifyReq(IdentifyReq other) {
    if (other.isSetUserName()) {
      this.UserName = other.UserName;
    }
    if (other.isSetPasswdMd5()) {
      this.PasswdMd5 = other.PasswdMd5;
    }
  }

  public IdentifyReq deepCopy() {
    return new IdentifyReq(this);
  }

  @Override
  public void clear() {
    this.UserName = null;
    this.PasswdMd5 = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getUserName() {
    return this.UserName;
  }

  public IdentifyReq setUserName(@org.apache.thrift.annotation.Nullable java.lang.String UserName) {
    this.UserName = UserName;
    return this;
  }

  public void unsetUserName() {
    this.UserName = null;
  }

  /** Returns true if field UserName is set (has been assigned a value) and false otherwise */
  public boolean isSetUserName() {
    return this.UserName != null;
  }

  public void setUserNameIsSet(boolean value) {
    if (!value) {
      this.UserName = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getPasswdMd5() {
    return this.PasswdMd5;
  }

  public IdentifyReq setPasswdMd5(@org.apache.thrift.annotation.Nullable java.lang.String PasswdMd5) {
    this.PasswdMd5 = PasswdMd5;
    return this;
  }

  public void unsetPasswdMd5() {
    this.PasswdMd5 = null;
  }

  /** Returns true if field PasswdMd5 is set (has been assigned a value) and false otherwise */
  public boolean isSetPasswdMd5() {
    return this.PasswdMd5 != null;
  }

  public void setPasswdMd5IsSet(boolean value) {
    if (!value) {
      this.PasswdMd5 = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case USER_NAME:
      if (value == null) {
        unsetUserName();
      } else {
        setUserName((java.lang.String)value);
      }
      break;

    case PASSWD_MD5:
      if (value == null) {
        unsetPasswdMd5();
      } else {
        setPasswdMd5((java.lang.String)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case USER_NAME:
      return getUserName();

    case PASSWD_MD5:
      return getPasswdMd5();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case USER_NAME:
      return isSetUserName();
    case PASSWD_MD5:
      return isSetPasswdMd5();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof IdentifyReq)
      return this.equals((IdentifyReq)that);
    return false;
  }

  public boolean equals(IdentifyReq that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_UserName = true && this.isSetUserName();
    boolean that_present_UserName = true && that.isSetUserName();
    if (this_present_UserName || that_present_UserName) {
      if (!(this_present_UserName && that_present_UserName))
        return false;
      if (!this.UserName.equals(that.UserName))
        return false;
    }

    boolean this_present_PasswdMd5 = true && this.isSetPasswdMd5();
    boolean that_present_PasswdMd5 = true && that.isSetPasswdMd5();
    if (this_present_PasswdMd5 || that_present_PasswdMd5) {
      if (!(this_present_PasswdMd5 && that_present_PasswdMd5))
        return false;
      if (!this.PasswdMd5.equals(that.PasswdMd5))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetUserName()) ? 131071 : 524287);
    if (isSetUserName())
      hashCode = hashCode * 8191 + UserName.hashCode();

    hashCode = hashCode * 8191 + ((isSetPasswdMd5()) ? 131071 : 524287);
    if (isSetPasswdMd5())
      hashCode = hashCode * 8191 + PasswdMd5.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(IdentifyReq other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetUserName(), other.isSetUserName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.UserName, other.UserName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetPasswdMd5(), other.isSetPasswdMd5());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPasswdMd5()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.PasswdMd5, other.PasswdMd5);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("IdentifyReq(");
    boolean first = true;

    sb.append("UserName:");
    if (this.UserName == null) {
      sb.append("null");
    } else {
      sb.append(this.UserName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("PasswdMd5:");
    if (this.PasswdMd5 == null) {
      sb.append("null");
    } else {
      sb.append(this.PasswdMd5);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (UserName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'UserName' was not present! Struct: " + toString());
    }
    if (PasswdMd5 == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'PasswdMd5' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class IdentifyReqStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public IdentifyReqStandardScheme getScheme() {
      return new IdentifyReqStandardScheme();
    }
  }

  private static class IdentifyReqStandardScheme extends org.apache.thrift.scheme.StandardScheme<IdentifyReq> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, IdentifyReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // USER_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.UserName = iprot.readString();
              struct.setUserNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PASSWD_MD5
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.PasswdMd5 = iprot.readString();
              struct.setPasswdMd5IsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, IdentifyReq struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.UserName != null) {
        oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
        oprot.writeString(struct.UserName);
        oprot.writeFieldEnd();
      }
      if (struct.PasswdMd5 != null) {
        oprot.writeFieldBegin(PASSWD_MD5_FIELD_DESC);
        oprot.writeString(struct.PasswdMd5);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class IdentifyReqTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public IdentifyReqTupleScheme getScheme() {
      return new IdentifyReqTupleScheme();
    }
  }

  private static class IdentifyReqTupleScheme extends org.apache.thrift.scheme.TupleScheme<IdentifyReq> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, IdentifyReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.UserName);
      oprot.writeString(struct.PasswdMd5);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, IdentifyReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.UserName = iprot.readString();
      struct.setUserNameIsSet(true);
      struct.PasswdMd5 = iprot.readString();
      struct.setPasswdMd5IsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
