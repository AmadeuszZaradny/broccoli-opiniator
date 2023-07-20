package pl.ama.client.broccoli

import okhttp3.ResponseBody

class CouldNotFindSessionIdException(response: ResponseBody):
    RuntimeException("Could not find session id in broccoli response: $response")
