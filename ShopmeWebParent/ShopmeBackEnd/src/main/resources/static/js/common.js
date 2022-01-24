$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
	
	customizeDropdownMenu();
});

function customizeDropdownMenu() {
	
	$(".navbar-item.dropdown").hover(
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
			},
			function() {
				$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideUp();
			});

	$(".dropdown > a").click(function() {
		location.href = this.href;
	});
}