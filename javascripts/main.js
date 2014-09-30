jQuery(document).ready(function() {
  jQuery(".solution").wrap('<div class = "spoiler"></div>')
  jQuery(".solution").before('<button  type="button" class = "teaser">Afficher une aide</button>')
  jQuery(".solution").hide();
  jQuery(document).on('click', '.teaser', function() {
    jQuery(this).next(".solution").slideToggle(500);
  });
});
