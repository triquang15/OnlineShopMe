$(document).ready(function() {
	$("#buttonAdd2Cart").on("click", function(evt) {
		addToCart();
	});
});

function addToCart() {
	quantity = $("#quantity" + productId).val();
	url = contextPath + "cart/add/" + productId + "/" + quantity;

	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(x) {
			x.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(res) {
		showModalDialog("Shopping Cart", res);
	}).fail(function() {
		showErrorModal("Error while adding product to shopping cart.");
	});
}