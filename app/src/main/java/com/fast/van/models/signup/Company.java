package com.fast.van.models.signup;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 22/09/15.
 */
public class Company {

  @Expose private String _id;
    @Expose private String companyName;
    @Expose private String email;
    @Expose private String fullName;

  public String get_id() {
    return _id;
  }

  public String getCompanyName() {
    return companyName;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return fullName;
  }
}
