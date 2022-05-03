package pl.ama.client.broccoli

import com.squareup.okhttp.ResponseBody

class CouldNotFindSessionIdException(response: ResponseBody):
    RuntimeException("Could not find session id in broccoli response: $response")