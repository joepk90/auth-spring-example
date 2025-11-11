package com.springauthapi.authservice.policies;

public class PolicyConstants {

    private PolicyConstants() {} // prevent instantiation

    /**
     * ACTION TYPES
     */
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_READ = "read";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";

    /**
     * RESOURCE TYPES
     */
    public static final String RESOURCE_POST = "post";

    /**
     * DEFAULT RESOURCE ID
     */
    public static final String RESOURCE_ID = "*";

    /**
     * DEFAULT RESOURCE ONWER ID
     */
    public static final String RESOURCE_OWNER_ID = "";

     /**
     * RESOURCE EFFECT RESULTS
     */
    public static final String RESOURCE_EFFECT_ALLOW = "EFFECT_ALLOW";
}
