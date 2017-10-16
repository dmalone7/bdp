/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.refactorlabs.cs378.sessions;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Event extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Event\",\"namespace\":\"com.refactorlabs.cs378.sessions\",\"fields\":[{\"name\":\"event_type\",\"type\":{\"type\":\"enum\",\"name\":\"EventType\",\"symbols\":[\"CHANGE\",\"CLICK\",\"DISPLAY\",\"EDIT\",\"SHOW\",\"SUBMIT\",\"VISIT\"]}},{\"name\":\"event_subtype\",\"type\":{\"type\":\"enum\",\"name\":\"EventSubtype\",\"symbols\":[\"CONTACT_FORM\",\"CONTACT_BANNER\",\"CONTACT_BUTTON\",\"DEALER_PHONE\",\"FEATURES\",\"GET_DIRECTIONS\",\"VEHICLE_HISTORY\",\"ALTERNATIVE\",\"BADGE_DETAIL\",\"PHOTO_MODAL\",\"BADGES\",\"MARKET_REPORT\"]}},{\"name\":\"event_time\",\"type\":\"string\"},{\"name\":\"city\",\"type\":[\"null\",\"string\"]},{\"name\":\"vin\",\"type\":\"string\"},{\"name\":\"condition\",\"type\":{\"type\":\"enum\",\"name\":\"Condition\",\"symbols\":[\"NEW\",\"USED\"]}},{\"name\":\"year\",\"type\":\"int\"},{\"name\":\"make\",\"type\":\"string\"},{\"name\":\"model\",\"type\":\"string\"},{\"name\":\"trim\",\"type\":[\"null\",\"string\"]},{\"name\":\"body_style\",\"type\":{\"type\":\"enum\",\"name\":\"BodyStyle\",\"symbols\":[\"CASSIS\",\"CONVERTIBLE\",\"COUPE\",\"HATCHBACK\",\"MINIVAN\",\"PICKUP\",\"SUV\",\"SEDAN\",\"VAN\",\"WAGON\"]}},{\"name\":\"cab_style\",\"type\":[\"null\",{\"type\":\"enum\",\"name\":\"CabStyle\",\"symbols\":[\"CREW\",\"EXTENDED\",\"REGULAR\"]}]},{\"name\":\"price\",\"type\":\"double\"},{\"name\":\"mileage\",\"type\":\"long\"},{\"name\":\"free_carfax_report\",\"type\":\"boolean\"},{\"name\":\"features\",\"type\":{\"type\":\"array\",\"items\":\"string\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public com.refactorlabs.cs378.sessions.EventType event_type;
  @Deprecated public com.refactorlabs.cs378.sessions.EventSubtype event_subtype;
  @Deprecated public java.lang.CharSequence event_time;
  @Deprecated public java.lang.CharSequence city;
  @Deprecated public java.lang.CharSequence vin;
  @Deprecated public com.refactorlabs.cs378.sessions.Condition condition;
  @Deprecated public int year;
  @Deprecated public java.lang.CharSequence make;
  @Deprecated public java.lang.CharSequence model;
  @Deprecated public java.lang.CharSequence trim;
  @Deprecated public com.refactorlabs.cs378.sessions.BodyStyle body_style;
  @Deprecated public com.refactorlabs.cs378.sessions.CabStyle cab_style;
  @Deprecated public double price;
  @Deprecated public long mileage;
  @Deprecated public boolean free_carfax_report;
  @Deprecated public java.util.List<java.lang.CharSequence> features;

  /**
   * Default constructor.
   */
  public Event() {}

  /**
   * All-args constructor.
   */
  public Event(com.refactorlabs.cs378.sessions.EventType event_type, com.refactorlabs.cs378.sessions.EventSubtype event_subtype, java.lang.CharSequence event_time, java.lang.CharSequence city, java.lang.CharSequence vin, com.refactorlabs.cs378.sessions.Condition condition, java.lang.Integer year, java.lang.CharSequence make, java.lang.CharSequence model, java.lang.CharSequence trim, com.refactorlabs.cs378.sessions.BodyStyle body_style, com.refactorlabs.cs378.sessions.CabStyle cab_style, java.lang.Double price, java.lang.Long mileage, java.lang.Boolean free_carfax_report, java.util.List<java.lang.CharSequence> features) {
    this.event_type = event_type;
    this.event_subtype = event_subtype;
    this.event_time = event_time;
    this.city = city;
    this.vin = vin;
    this.condition = condition;
    this.year = year;
    this.make = make;
    this.model = model;
    this.trim = trim;
    this.body_style = body_style;
    this.cab_style = cab_style;
    this.price = price;
    this.mileage = mileage;
    this.free_carfax_report = free_carfax_report;
    this.features = features;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return event_type;
    case 1: return event_subtype;
    case 2: return event_time;
    case 3: return city;
    case 4: return vin;
    case 5: return condition;
    case 6: return year;
    case 7: return make;
    case 8: return model;
    case 9: return trim;
    case 10: return body_style;
    case 11: return cab_style;
    case 12: return price;
    case 13: return mileage;
    case 14: return free_carfax_report;
    case 15: return features;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: event_type = (com.refactorlabs.cs378.sessions.EventType)value$; break;
    case 1: event_subtype = (com.refactorlabs.cs378.sessions.EventSubtype)value$; break;
    case 2: event_time = (java.lang.CharSequence)value$; break;
    case 3: city = (java.lang.CharSequence)value$; break;
    case 4: vin = (java.lang.CharSequence)value$; break;
    case 5: condition = (com.refactorlabs.cs378.sessions.Condition)value$; break;
    case 6: year = (java.lang.Integer)value$; break;
    case 7: make = (java.lang.CharSequence)value$; break;
    case 8: model = (java.lang.CharSequence)value$; break;
    case 9: trim = (java.lang.CharSequence)value$; break;
    case 10: body_style = (com.refactorlabs.cs378.sessions.BodyStyle)value$; break;
    case 11: cab_style = (com.refactorlabs.cs378.sessions.CabStyle)value$; break;
    case 12: price = (java.lang.Double)value$; break;
    case 13: mileage = (java.lang.Long)value$; break;
    case 14: free_carfax_report = (java.lang.Boolean)value$; break;
    case 15: features = (java.util.List<java.lang.CharSequence>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'event_type' field.
   */
  public com.refactorlabs.cs378.sessions.EventType getEventType() {
    return event_type;
  }

  /**
   * Sets the value of the 'event_type' field.
   * @param value the value to set.
   */
  public void setEventType(com.refactorlabs.cs378.sessions.EventType value) {
    this.event_type = value;
  }

  /**
   * Gets the value of the 'event_subtype' field.
   */
  public com.refactorlabs.cs378.sessions.EventSubtype getEventSubtype() {
    return event_subtype;
  }

  /**
   * Sets the value of the 'event_subtype' field.
   * @param value the value to set.
   */
  public void setEventSubtype(com.refactorlabs.cs378.sessions.EventSubtype value) {
    this.event_subtype = value;
  }

  /**
   * Gets the value of the 'event_time' field.
   */
  public java.lang.CharSequence getEventTime() {
    return event_time;
  }

  /**
   * Sets the value of the 'event_time' field.
   * @param value the value to set.
   */
  public void setEventTime(java.lang.CharSequence value) {
    this.event_time = value;
  }

  /**
   * Gets the value of the 'city' field.
   */
  public java.lang.CharSequence getCity() {
    return city;
  }

  /**
   * Sets the value of the 'city' field.
   * @param value the value to set.
   */
  public void setCity(java.lang.CharSequence value) {
    this.city = value;
  }

  /**
   * Gets the value of the 'vin' field.
   */
  public java.lang.CharSequence getVin() {
    return vin;
  }

  /**
   * Sets the value of the 'vin' field.
   * @param value the value to set.
   */
  public void setVin(java.lang.CharSequence value) {
    this.vin = value;
  }

  /**
   * Gets the value of the 'condition' field.
   */
  public com.refactorlabs.cs378.sessions.Condition getCondition() {
    return condition;
  }

  /**
   * Sets the value of the 'condition' field.
   * @param value the value to set.
   */
  public void setCondition(com.refactorlabs.cs378.sessions.Condition value) {
    this.condition = value;
  }

  /**
   * Gets the value of the 'year' field.
   */
  public java.lang.Integer getYear() {
    return year;
  }

  /**
   * Sets the value of the 'year' field.
   * @param value the value to set.
   */
  public void setYear(java.lang.Integer value) {
    this.year = value;
  }

  /**
   * Gets the value of the 'make' field.
   */
  public java.lang.CharSequence getMake() {
    return make;
  }

  /**
   * Sets the value of the 'make' field.
   * @param value the value to set.
   */
  public void setMake(java.lang.CharSequence value) {
    this.make = value;
  }

  /**
   * Gets the value of the 'model' field.
   */
  public java.lang.CharSequence getModel() {
    return model;
  }

  /**
   * Sets the value of the 'model' field.
   * @param value the value to set.
   */
  public void setModel(java.lang.CharSequence value) {
    this.model = value;
  }

  /**
   * Gets the value of the 'trim' field.
   */
  public java.lang.CharSequence getTrim() {
    return trim;
  }

  /**
   * Sets the value of the 'trim' field.
   * @param value the value to set.
   */
  public void setTrim(java.lang.CharSequence value) {
    this.trim = value;
  }

  /**
   * Gets the value of the 'body_style' field.
   */
  public com.refactorlabs.cs378.sessions.BodyStyle getBodyStyle() {
    return body_style;
  }

  /**
   * Sets the value of the 'body_style' field.
   * @param value the value to set.
   */
  public void setBodyStyle(com.refactorlabs.cs378.sessions.BodyStyle value) {
    this.body_style = value;
  }

  /**
   * Gets the value of the 'cab_style' field.
   */
  public com.refactorlabs.cs378.sessions.CabStyle getCabStyle() {
    return cab_style;
  }

  /**
   * Sets the value of the 'cab_style' field.
   * @param value the value to set.
   */
  public void setCabStyle(com.refactorlabs.cs378.sessions.CabStyle value) {
    this.cab_style = value;
  }

  /**
   * Gets the value of the 'price' field.
   */
  public java.lang.Double getPrice() {
    return price;
  }

  /**
   * Sets the value of the 'price' field.
   * @param value the value to set.
   */
  public void setPrice(java.lang.Double value) {
    this.price = value;
  }

  /**
   * Gets the value of the 'mileage' field.
   */
  public java.lang.Long getMileage() {
    return mileage;
  }

  /**
   * Sets the value of the 'mileage' field.
   * @param value the value to set.
   */
  public void setMileage(java.lang.Long value) {
    this.mileage = value;
  }

  /**
   * Gets the value of the 'free_carfax_report' field.
   */
  public java.lang.Boolean getFreeCarfaxReport() {
    return free_carfax_report;
  }

  /**
   * Sets the value of the 'free_carfax_report' field.
   * @param value the value to set.
   */
  public void setFreeCarfaxReport(java.lang.Boolean value) {
    this.free_carfax_report = value;
  }

  /**
   * Gets the value of the 'features' field.
   */
  public java.util.List<java.lang.CharSequence> getFeatures() {
    return features;
  }

  /**
   * Sets the value of the 'features' field.
   * @param value the value to set.
   */
  public void setFeatures(java.util.List<java.lang.CharSequence> value) {
    this.features = value;
  }

  /** Creates a new Event RecordBuilder */
  public static com.refactorlabs.cs378.sessions.Event.Builder newBuilder() {
    return new com.refactorlabs.cs378.sessions.Event.Builder();
  }
  
  /** Creates a new Event RecordBuilder by copying an existing Builder */
  public static com.refactorlabs.cs378.sessions.Event.Builder newBuilder(com.refactorlabs.cs378.sessions.Event.Builder other) {
    return new com.refactorlabs.cs378.sessions.Event.Builder(other);
  }
  
  /** Creates a new Event RecordBuilder by copying an existing Event instance */
  public static com.refactorlabs.cs378.sessions.Event.Builder newBuilder(com.refactorlabs.cs378.sessions.Event other) {
    return new com.refactorlabs.cs378.sessions.Event.Builder(other);
  }
  
  /**
   * RecordBuilder for Event instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Event>
    implements org.apache.avro.data.RecordBuilder<Event> {

    private com.refactorlabs.cs378.sessions.EventType event_type;
    private com.refactorlabs.cs378.sessions.EventSubtype event_subtype;
    private java.lang.CharSequence event_time;
    private java.lang.CharSequence city;
    private java.lang.CharSequence vin;
    private com.refactorlabs.cs378.sessions.Condition condition;
    private int year;
    private java.lang.CharSequence make;
    private java.lang.CharSequence model;
    private java.lang.CharSequence trim;
    private com.refactorlabs.cs378.sessions.BodyStyle body_style;
    private com.refactorlabs.cs378.sessions.CabStyle cab_style;
    private double price;
    private long mileage;
    private boolean free_carfax_report;
    private java.util.List<java.lang.CharSequence> features;

    /** Creates a new Builder */
    private Builder() {
      super(com.refactorlabs.cs378.sessions.Event.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.refactorlabs.cs378.sessions.Event.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing Event instance */
    private Builder(com.refactorlabs.cs378.sessions.Event other) {
            super(com.refactorlabs.cs378.sessions.Event.SCHEMA$);
      if (isValidValue(fields()[0], other.event_type)) {
        this.event_type = data().deepCopy(fields()[0].schema(), other.event_type);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.event_subtype)) {
        this.event_subtype = data().deepCopy(fields()[1].schema(), other.event_subtype);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.event_time)) {
        this.event_time = data().deepCopy(fields()[2].schema(), other.event_time);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.city)) {
        this.city = data().deepCopy(fields()[3].schema(), other.city);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.vin)) {
        this.vin = data().deepCopy(fields()[4].schema(), other.vin);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.condition)) {
        this.condition = data().deepCopy(fields()[5].schema(), other.condition);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.year)) {
        this.year = data().deepCopy(fields()[6].schema(), other.year);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.make)) {
        this.make = data().deepCopy(fields()[7].schema(), other.make);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.model)) {
        this.model = data().deepCopy(fields()[8].schema(), other.model);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.trim)) {
        this.trim = data().deepCopy(fields()[9].schema(), other.trim);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.body_style)) {
        this.body_style = data().deepCopy(fields()[10].schema(), other.body_style);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.cab_style)) {
        this.cab_style = data().deepCopy(fields()[11].schema(), other.cab_style);
        fieldSetFlags()[11] = true;
      }
      if (isValidValue(fields()[12], other.price)) {
        this.price = data().deepCopy(fields()[12].schema(), other.price);
        fieldSetFlags()[12] = true;
      }
      if (isValidValue(fields()[13], other.mileage)) {
        this.mileage = data().deepCopy(fields()[13].schema(), other.mileage);
        fieldSetFlags()[13] = true;
      }
      if (isValidValue(fields()[14], other.free_carfax_report)) {
        this.free_carfax_report = data().deepCopy(fields()[14].schema(), other.free_carfax_report);
        fieldSetFlags()[14] = true;
      }
      if (isValidValue(fields()[15], other.features)) {
        this.features = data().deepCopy(fields()[15].schema(), other.features);
        fieldSetFlags()[15] = true;
      }
    }

    /** Gets the value of the 'event_type' field */
    public com.refactorlabs.cs378.sessions.EventType getEventType() {
      return event_type;
    }
    
    /** Sets the value of the 'event_type' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setEventType(com.refactorlabs.cs378.sessions.EventType value) {
      validate(fields()[0], value);
      this.event_type = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'event_type' field has been set */
    public boolean hasEventType() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'event_type' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearEventType() {
      event_type = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'event_subtype' field */
    public com.refactorlabs.cs378.sessions.EventSubtype getEventSubtype() {
      return event_subtype;
    }
    
    /** Sets the value of the 'event_subtype' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setEventSubtype(com.refactorlabs.cs378.sessions.EventSubtype value) {
      validate(fields()[1], value);
      this.event_subtype = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'event_subtype' field has been set */
    public boolean hasEventSubtype() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'event_subtype' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearEventSubtype() {
      event_subtype = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'event_time' field */
    public java.lang.CharSequence getEventTime() {
      return event_time;
    }
    
    /** Sets the value of the 'event_time' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setEventTime(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.event_time = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'event_time' field has been set */
    public boolean hasEventTime() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'event_time' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearEventTime() {
      event_time = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'city' field */
    public java.lang.CharSequence getCity() {
      return city;
    }
    
    /** Sets the value of the 'city' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setCity(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.city = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'city' field has been set */
    public boolean hasCity() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'city' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearCity() {
      city = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'vin' field */
    public java.lang.CharSequence getVin() {
      return vin;
    }
    
    /** Sets the value of the 'vin' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setVin(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.vin = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'vin' field has been set */
    public boolean hasVin() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'vin' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearVin() {
      vin = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'condition' field */
    public com.refactorlabs.cs378.sessions.Condition getCondition() {
      return condition;
    }
    
    /** Sets the value of the 'condition' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setCondition(com.refactorlabs.cs378.sessions.Condition value) {
      validate(fields()[5], value);
      this.condition = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'condition' field has been set */
    public boolean hasCondition() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'condition' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearCondition() {
      condition = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'year' field */
    public java.lang.Integer getYear() {
      return year;
    }
    
    /** Sets the value of the 'year' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setYear(int value) {
      validate(fields()[6], value);
      this.year = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'year' field has been set */
    public boolean hasYear() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'year' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearYear() {
      fieldSetFlags()[6] = false;
      return this;
    }

    /** Gets the value of the 'make' field */
    public java.lang.CharSequence getMake() {
      return make;
    }
    
    /** Sets the value of the 'make' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setMake(java.lang.CharSequence value) {
      validate(fields()[7], value);
      this.make = value;
      fieldSetFlags()[7] = true;
      return this; 
    }
    
    /** Checks whether the 'make' field has been set */
    public boolean hasMake() {
      return fieldSetFlags()[7];
    }
    
    /** Clears the value of the 'make' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearMake() {
      make = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /** Gets the value of the 'model' field */
    public java.lang.CharSequence getModel() {
      return model;
    }
    
    /** Sets the value of the 'model' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setModel(java.lang.CharSequence value) {
      validate(fields()[8], value);
      this.model = value;
      fieldSetFlags()[8] = true;
      return this; 
    }
    
    /** Checks whether the 'model' field has been set */
    public boolean hasModel() {
      return fieldSetFlags()[8];
    }
    
    /** Clears the value of the 'model' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearModel() {
      model = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /** Gets the value of the 'trim' field */
    public java.lang.CharSequence getTrim() {
      return trim;
    }
    
    /** Sets the value of the 'trim' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setTrim(java.lang.CharSequence value) {
      validate(fields()[9], value);
      this.trim = value;
      fieldSetFlags()[9] = true;
      return this; 
    }
    
    /** Checks whether the 'trim' field has been set */
    public boolean hasTrim() {
      return fieldSetFlags()[9];
    }
    
    /** Clears the value of the 'trim' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearTrim() {
      trim = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    /** Gets the value of the 'body_style' field */
    public com.refactorlabs.cs378.sessions.BodyStyle getBodyStyle() {
      return body_style;
    }
    
    /** Sets the value of the 'body_style' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setBodyStyle(com.refactorlabs.cs378.sessions.BodyStyle value) {
      validate(fields()[10], value);
      this.body_style = value;
      fieldSetFlags()[10] = true;
      return this; 
    }
    
    /** Checks whether the 'body_style' field has been set */
    public boolean hasBodyStyle() {
      return fieldSetFlags()[10];
    }
    
    /** Clears the value of the 'body_style' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearBodyStyle() {
      body_style = null;
      fieldSetFlags()[10] = false;
      return this;
    }

    /** Gets the value of the 'cab_style' field */
    public com.refactorlabs.cs378.sessions.CabStyle getCabStyle() {
      return cab_style;
    }
    
    /** Sets the value of the 'cab_style' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setCabStyle(com.refactorlabs.cs378.sessions.CabStyle value) {
      validate(fields()[11], value);
      this.cab_style = value;
      fieldSetFlags()[11] = true;
      return this; 
    }
    
    /** Checks whether the 'cab_style' field has been set */
    public boolean hasCabStyle() {
      return fieldSetFlags()[11];
    }
    
    /** Clears the value of the 'cab_style' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearCabStyle() {
      cab_style = null;
      fieldSetFlags()[11] = false;
      return this;
    }

    /** Gets the value of the 'price' field */
    public java.lang.Double getPrice() {
      return price;
    }
    
    /** Sets the value of the 'price' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setPrice(double value) {
      validate(fields()[12], value);
      this.price = value;
      fieldSetFlags()[12] = true;
      return this; 
    }
    
    /** Checks whether the 'price' field has been set */
    public boolean hasPrice() {
      return fieldSetFlags()[12];
    }
    
    /** Clears the value of the 'price' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearPrice() {
      fieldSetFlags()[12] = false;
      return this;
    }

    /** Gets the value of the 'mileage' field */
    public java.lang.Long getMileage() {
      return mileage;
    }
    
    /** Sets the value of the 'mileage' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setMileage(long value) {
      validate(fields()[13], value);
      this.mileage = value;
      fieldSetFlags()[13] = true;
      return this; 
    }
    
    /** Checks whether the 'mileage' field has been set */
    public boolean hasMileage() {
      return fieldSetFlags()[13];
    }
    
    /** Clears the value of the 'mileage' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearMileage() {
      fieldSetFlags()[13] = false;
      return this;
    }

    /** Gets the value of the 'free_carfax_report' field */
    public java.lang.Boolean getFreeCarfaxReport() {
      return free_carfax_report;
    }
    
    /** Sets the value of the 'free_carfax_report' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setFreeCarfaxReport(boolean value) {
      validate(fields()[14], value);
      this.free_carfax_report = value;
      fieldSetFlags()[14] = true;
      return this; 
    }
    
    /** Checks whether the 'free_carfax_report' field has been set */
    public boolean hasFreeCarfaxReport() {
      return fieldSetFlags()[14];
    }
    
    /** Clears the value of the 'free_carfax_report' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearFreeCarfaxReport() {
      fieldSetFlags()[14] = false;
      return this;
    }

    /** Gets the value of the 'features' field */
    public java.util.List<java.lang.CharSequence> getFeatures() {
      return features;
    }
    
    /** Sets the value of the 'features' field */
    public com.refactorlabs.cs378.sessions.Event.Builder setFeatures(java.util.List<java.lang.CharSequence> value) {
      validate(fields()[15], value);
      this.features = value;
      fieldSetFlags()[15] = true;
      return this; 
    }
    
    /** Checks whether the 'features' field has been set */
    public boolean hasFeatures() {
      return fieldSetFlags()[15];
    }
    
    /** Clears the value of the 'features' field */
    public com.refactorlabs.cs378.sessions.Event.Builder clearFeatures() {
      features = null;
      fieldSetFlags()[15] = false;
      return this;
    }

    @Override
    public Event build() {
      try {
        Event record = new Event();
        record.event_type = fieldSetFlags()[0] ? this.event_type : (com.refactorlabs.cs378.sessions.EventType) defaultValue(fields()[0]);
        record.event_subtype = fieldSetFlags()[1] ? this.event_subtype : (com.refactorlabs.cs378.sessions.EventSubtype) defaultValue(fields()[1]);
        record.event_time = fieldSetFlags()[2] ? this.event_time : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.city = fieldSetFlags()[3] ? this.city : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.vin = fieldSetFlags()[4] ? this.vin : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.condition = fieldSetFlags()[5] ? this.condition : (com.refactorlabs.cs378.sessions.Condition) defaultValue(fields()[5]);
        record.year = fieldSetFlags()[6] ? this.year : (java.lang.Integer) defaultValue(fields()[6]);
        record.make = fieldSetFlags()[7] ? this.make : (java.lang.CharSequence) defaultValue(fields()[7]);
        record.model = fieldSetFlags()[8] ? this.model : (java.lang.CharSequence) defaultValue(fields()[8]);
        record.trim = fieldSetFlags()[9] ? this.trim : (java.lang.CharSequence) defaultValue(fields()[9]);
        record.body_style = fieldSetFlags()[10] ? this.body_style : (com.refactorlabs.cs378.sessions.BodyStyle) defaultValue(fields()[10]);
        record.cab_style = fieldSetFlags()[11] ? this.cab_style : (com.refactorlabs.cs378.sessions.CabStyle) defaultValue(fields()[11]);
        record.price = fieldSetFlags()[12] ? this.price : (java.lang.Double) defaultValue(fields()[12]);
        record.mileage = fieldSetFlags()[13] ? this.mileage : (java.lang.Long) defaultValue(fields()[13]);
        record.free_carfax_report = fieldSetFlags()[14] ? this.free_carfax_report : (java.lang.Boolean) defaultValue(fields()[14]);
        record.features = fieldSetFlags()[15] ? this.features : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[15]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
