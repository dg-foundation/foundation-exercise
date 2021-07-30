package com.foundation.mbta.data.response;

import lombok.Data;

import java.util.List;

/**
 * JSON Response data is mapped to this class by Gson
 */
@Data
public class MbtaArrayResponse {
    List<MbtaResponseData> data;
}
