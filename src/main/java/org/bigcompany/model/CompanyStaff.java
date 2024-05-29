package org.bigcompany.model;

import java.math.BigDecimal;

public interface CompanyStaff {
    String getId();

    String getFirstName();

    String getLastName();

    BigDecimal getSalary();

    String getManagerId();

    String getReportingLineLength();

    void setReportingLineLength(String reportingLineLength);
}
