package com.foundation.mbta.data.response;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * Represents mapping of response data
 */
@Data
public class MbtaResponseData {
    String id;
    String type;
    LinkedHashMap attributes;
    LinkedHashMap links;
    LinkedHashMap relationships;
}
