package com.fb.vo;

import java.io.Serializable;
import java.util.Comparator;

public class LabelValueBean implements Comparable<Object>, Serializable {

	private static final long serialVersionUID = 7182636223722667373L;

    private String label;
    private String value;

	/**
     * Comparator that can be used for a case insensitive sort of 
     * <code>LabelValueBean</code> objects.
     */
    public static final Comparator<Object> CASE_INSENSITIVE_ORDER = new Comparator<Object>() {
    	public int compare(Object o1, Object o2) {
    		String label1 = ((LabelValueBean) o1).getLabel();
    		String label2 = ((LabelValueBean) o2).getLabel();
    		return label1.compareToIgnoreCase(label2);
    	}
    };

    public LabelValueBean() {
        super();
    }
    
    /**
     * Construct an instance with the supplied property values.
     *
     * @param label The label to be displayed to the user.
     * @param value The value to be returned to the server.
     */
    public LabelValueBean(String label, String value) {
        this.label = label;
        this.value = value;
    }

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Compare LabelValueBeans based on the label, because that's the human 
     * viewable part of the object.
	 * @see Comparable
	 */
	public int compareTo(Object o) {
		// Implicitly tests for the correct type, throwing 
        // ClassCastException as required by interface
		String otherLabel = ((LabelValueBean) o).getLabel();

		return this.getLabel().compareTo(otherLabel);
	}

    public String toString() {
        StringBuffer sb = new StringBuffer("LabelValueBean[");
        sb.append(this.label);
        sb.append(", ");
        sb.append(this.value);
        sb.append("]");
        return sb.toString();
    }

    /**
     * LabelValueBeans are equal if their values are both null or equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof LabelValueBean)) {
            return false;
        }

        LabelValueBean bean = (LabelValueBean) obj;
        int nil = (this.getValue() == null) ? 1 : 0;
        nil += (bean.getValue() == null) ? 1 : 0;

        if (nil == 2) {
            return true;
        } else if (nil == 1) {
            return false;
        } else {
            return this.getValue().equals(bean.getValue());
        }

    }

    /**
     * The hash code is based on the object's value.
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (this.getValue() == null) ? 17 : this.getValue().hashCode();
    }
}
