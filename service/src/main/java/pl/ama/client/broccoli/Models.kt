package pl.ama.client.broccoli

import com.fasterxml.jackson.annotation.JsonProperty

data class BroccoliSession(
    val sessionId: String
)

data class DietToRate(
    val menuId: Int,
    val date: String,
    val productId: Int,
    val mealTimeId: Int,
    val orderItemId: Int
)

data class RateDietRequest(
    @get:JsonProperty("opinion") val opinion: Opinion
){
    data class Opinion(
        @get:JsonProperty("menu_id") val menuId: Int,
        @get:JsonProperty("date") val date: String,
        @get:JsonProperty("stars") val stars: Int,
        @get:JsonProperty("note") val note: String,
        @get:JsonProperty("product_id") val productId: Int,
        @get:JsonProperty("meal_time_id") val mealTimeId: Int,
        @get:JsonProperty("order_item_id") val orderItemId: Int
    )
}

data class UserData(
    @JsonProperty("customer") val customer: Customer,
    @JsonProperty("rewards") val rewards: Rewards
) {
    data class Customer(@JsonProperty("fullname") val fullname: String)
    data class Rewards(@JsonProperty("amount") val amount: String)
}