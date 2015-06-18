var gulp = require('gulp');
var compass = require('gulp-compass');
var watch = require('gulp-watch');

gulp.task('compass', function() {
  gulp.src('./assets/sass/*.scss')
    .pipe(compass({
      css: 'assets/css',
      sass: 'assets/sass',
      image: 'assets/images'
    }))

    .on('error', function(error) {
      // Would like to catch the error here 
      console.log(error);
      this.emit('end');
    })

    .pipe(gulp.dest('assets/css'));
});

gulp.task('watch', function() {
  gulp.watch('assets/sass/**/*.scss', ['compass'])
})

gulp.task('default', ['compass', 'watch']);

