$(function() {
  // TODO: initialize Slick for navigating
  // TODO: like ajax call

  var current = 0;

  $('.contributions ul').slick({
    arrows: false,
    swipeToSlide: true,
    infinite: false
  });

  $('.contributions li').each(function() {
    var $this = $(this),
        length = $this.find('img').length,
        timer;

    $this.find('img').each(function(index) {
      $(this).css('z-index', length - index);
    })

    $this.find('.image-deck').bind('mouseenter touchstart', function(e) {
      e.preventDefault();
      current = 0;
      updateTimer($this, length);
    }).bind('mouseleave touchend', function(e){
      e.preventDefault();
      clearTimeout(timer);
      $this.find('img').css('display', 'block');
      current = 0;
    }) 

    $this.find('a.vote').click(function(e) {
      e.preventDefault();
      $(this).toggleClass('vote--voted');

      if(!$(this).data('amount')) {
        $(this).data('amount', parseInt($(this).find('.amount').text()));
      }

      if(parseInt($(this).find('.amount').text()) > $(this).data('amount')) {
        $(this).find('.amount').text($(this).data('amount'));
      } else {
        $(this).find('.amount').text($(this).data('amount') + 1);
      }
    })

    function updateTimer($this, length) {
      clearTimeout(timer);
      timer = setTimeout(function() {

        if(current < length-1) {
          $this.find('img').eq(current).css('display', 'none');
        } else {
          $this.find('img').css('display', 'block');
        }

        if(current == length-1) {
          current = 0;
        } else {
          current = current+1;
        }

        updateTimer($this, length);

      }, 500);
    }
  })
})