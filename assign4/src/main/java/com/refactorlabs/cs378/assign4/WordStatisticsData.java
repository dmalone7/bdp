/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.refactorlabs.cs378.assign4;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class WordStatisticsData extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"WordStatisticsData\",\"namespace\":\"com.refactorlabs.cs378.assign4\",\"fields\":[{\"name\":\"document_count\",\"type\":\"long\"},{\"name\":\"total_count\",\"type\":\"long\"},{\"name\":\"min\",\"type\":\"long\"},{\"name\":\"max\",\"type\":\"long\"},{\"name\":\"sum_of_squares\",\"type\":\"long\"},{\"name\":\"mean\",\"type\":\"double\"},{\"name\":\"variance\",\"type\":\"double\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public long document_count;
  @Deprecated public long total_count;
  @Deprecated public long min;
  @Deprecated public long max;
  @Deprecated public long sum_of_squares;
  @Deprecated public double mean;
  @Deprecated public double variance;

  /**
   * Default constructor.
   */
  public WordStatisticsData() {}

  /**
   * All-args constructor.
   */
  public WordStatisticsData(java.lang.Long document_count, java.lang.Long total_count, java.lang.Long min, java.lang.Long max, java.lang.Long sum_of_squares, java.lang.Double mean, java.lang.Double variance) {
    this.document_count = document_count;
    this.total_count = total_count;
    this.min = min;
    this.max = max;
    this.sum_of_squares = sum_of_squares;
    this.mean = mean;
    this.variance = variance;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return document_count;
    case 1: return total_count;
    case 2: return min;
    case 3: return max;
    case 4: return sum_of_squares;
    case 5: return mean;
    case 6: return variance;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: document_count = (java.lang.Long)value$; break;
    case 1: total_count = (java.lang.Long)value$; break;
    case 2: min = (java.lang.Long)value$; break;
    case 3: max = (java.lang.Long)value$; break;
    case 4: sum_of_squares = (java.lang.Long)value$; break;
    case 5: mean = (java.lang.Double)value$; break;
    case 6: variance = (java.lang.Double)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'document_count' field.
   */
  public java.lang.Long getDocumentCount() {
    return document_count;
  }

  /**
   * Sets the value of the 'document_count' field.
   * @param value the value to set.
   */
  public void setDocumentCount(java.lang.Long value) {
    this.document_count = value;
  }

  /**
   * Gets the value of the 'total_count' field.
   */
  public java.lang.Long getTotalCount() {
    return total_count;
  }

  /**
   * Sets the value of the 'total_count' field.
   * @param value the value to set.
   */
  public void setTotalCount(java.lang.Long value) {
    this.total_count = value;
  }

  /**
   * Gets the value of the 'min' field.
   */
  public java.lang.Long getMin() {
    return min;
  }

  /**
   * Sets the value of the 'min' field.
   * @param value the value to set.
   */
  public void setMin(java.lang.Long value) {
    this.min = value;
  }

  /**
   * Gets the value of the 'max' field.
   */
  public java.lang.Long getMax() {
    return max;
  }

  /**
   * Sets the value of the 'max' field.
   * @param value the value to set.
   */
  public void setMax(java.lang.Long value) {
    this.max = value;
  }

  /**
   * Gets the value of the 'sum_of_squares' field.
   */
  public java.lang.Long getSumOfSquares() {
    return sum_of_squares;
  }

  /**
   * Sets the value of the 'sum_of_squares' field.
   * @param value the value to set.
   */
  public void setSumOfSquares(java.lang.Long value) {
    this.sum_of_squares = value;
  }

  /**
   * Gets the value of the 'mean' field.
   */
  public java.lang.Double getMean() {
    return mean;
  }

  /**
   * Sets the value of the 'mean' field.
   * @param value the value to set.
   */
  public void setMean(java.lang.Double value) {
    this.mean = value;
  }

  /**
   * Gets the value of the 'variance' field.
   */
  public java.lang.Double getVariance() {
    return variance;
  }

  /**
   * Sets the value of the 'variance' field.
   * @param value the value to set.
   */
  public void setVariance(java.lang.Double value) {
    this.variance = value;
  }

  /** Creates a new WordStatisticsData RecordBuilder */
  public static com.refactorlabs.cs378.assign4.WordStatisticsData.Builder newBuilder() {
    return new com.refactorlabs.cs378.assign4.WordStatisticsData.Builder();
  }
  
  /** Creates a new WordStatisticsData RecordBuilder by copying an existing Builder */
  public static com.refactorlabs.cs378.assign4.WordStatisticsData.Builder newBuilder(com.refactorlabs.cs378.assign4.WordStatisticsData.Builder other) {
    return new com.refactorlabs.cs378.assign4.WordStatisticsData.Builder(other);
  }
  
  /** Creates a new WordStatisticsData RecordBuilder by copying an existing WordStatisticsData instance */
  public static com.refactorlabs.cs378.assign4.WordStatisticsData.Builder newBuilder(com.refactorlabs.cs378.assign4.WordStatisticsData other) {
    return new com.refactorlabs.cs378.assign4.WordStatisticsData.Builder(other);
  }
  
  /**
   * RecordBuilder for WordStatisticsData instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<WordStatisticsData>
    implements org.apache.avro.data.RecordBuilder<WordStatisticsData> {

    private long document_count;
    private long total_count;
    private long min;
    private long max;
    private long sum_of_squares;
    private double mean;
    private double variance;

    /** Creates a new Builder */
    private Builder() {
      super(com.refactorlabs.cs378.assign4.WordStatisticsData.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.refactorlabs.cs378.assign4.WordStatisticsData.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing WordStatisticsData instance */
    private Builder(com.refactorlabs.cs378.assign4.WordStatisticsData other) {
            super(com.refactorlabs.cs378.assign4.WordStatisticsData.SCHEMA$);
      if (isValidValue(fields()[0], other.document_count)) {
        this.document_count = data().deepCopy(fields()[0].schema(), other.document_count);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.total_count)) {
        this.total_count = data().deepCopy(fields()[1].schema(), other.total_count);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.min)) {
        this.min = data().deepCopy(fields()[2].schema(), other.min);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.max)) {
        this.max = data().deepCopy(fields()[3].schema(), other.max);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.sum_of_squares)) {
        this.sum_of_squares = data().deepCopy(fields()[4].schema(), other.sum_of_squares);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.mean)) {
        this.mean = data().deepCopy(fields()[5].schema(), other.mean);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.variance)) {
        this.variance = data().deepCopy(fields()[6].schema(), other.variance);
        fieldSetFlags()[6] = true;
      }
    }

    /** Gets the value of the 'document_count' field */
    public java.lang.Long getDocumentCount() {
      return document_count;
    }
    
    /** Sets the value of the 'document_count' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setDocumentCount(long value) {
      validate(fields()[0], value);
      this.document_count = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'document_count' field has been set */
    public boolean hasDocumentCount() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'document_count' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearDocumentCount() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'total_count' field */
    public java.lang.Long getTotalCount() {
      return total_count;
    }
    
    /** Sets the value of the 'total_count' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setTotalCount(long value) {
      validate(fields()[1], value);
      this.total_count = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'total_count' field has been set */
    public boolean hasTotalCount() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'total_count' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearTotalCount() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'min' field */
    public java.lang.Long getMin() {
      return min;
    }
    
    /** Sets the value of the 'min' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setMin(long value) {
      validate(fields()[2], value);
      this.min = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'min' field has been set */
    public boolean hasMin() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'min' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearMin() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'max' field */
    public java.lang.Long getMax() {
      return max;
    }
    
    /** Sets the value of the 'max' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setMax(long value) {
      validate(fields()[3], value);
      this.max = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'max' field has been set */
    public boolean hasMax() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'max' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearMax() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'sum_of_squares' field */
    public java.lang.Long getSumOfSquares() {
      return sum_of_squares;
    }
    
    /** Sets the value of the 'sum_of_squares' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setSumOfSquares(long value) {
      validate(fields()[4], value);
      this.sum_of_squares = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'sum_of_squares' field has been set */
    public boolean hasSumOfSquares() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'sum_of_squares' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearSumOfSquares() {
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'mean' field */
    public java.lang.Double getMean() {
      return mean;
    }
    
    /** Sets the value of the 'mean' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setMean(double value) {
      validate(fields()[5], value);
      this.mean = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'mean' field has been set */
    public boolean hasMean() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'mean' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearMean() {
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'variance' field */
    public java.lang.Double getVariance() {
      return variance;
    }
    
    /** Sets the value of the 'variance' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder setVariance(double value) {
      validate(fields()[6], value);
      this.variance = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'variance' field has been set */
    public boolean hasVariance() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'variance' field */
    public com.refactorlabs.cs378.assign4.WordStatisticsData.Builder clearVariance() {
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    public WordStatisticsData build() {
      try {
        WordStatisticsData record = new WordStatisticsData();
        record.document_count = fieldSetFlags()[0] ? this.document_count : (java.lang.Long) defaultValue(fields()[0]);
        record.total_count = fieldSetFlags()[1] ? this.total_count : (java.lang.Long) defaultValue(fields()[1]);
        record.min = fieldSetFlags()[2] ? this.min : (java.lang.Long) defaultValue(fields()[2]);
        record.max = fieldSetFlags()[3] ? this.max : (java.lang.Long) defaultValue(fields()[3]);
        record.sum_of_squares = fieldSetFlags()[4] ? this.sum_of_squares : (java.lang.Long) defaultValue(fields()[4]);
        record.mean = fieldSetFlags()[5] ? this.mean : (java.lang.Double) defaultValue(fields()[5]);
        record.variance = fieldSetFlags()[6] ? this.variance : (java.lang.Double) defaultValue(fields()[6]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
