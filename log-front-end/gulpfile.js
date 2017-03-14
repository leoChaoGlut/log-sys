var gulp = require('gulp'),
    connect = require('gulp-connect'),
    less = require('gulp-less'),
    cleanCSS = require('gulp-clean-css'),
    uglify = require('gulp-uglify'),
    htmlmin = require('gulp-html-minifier');

gulp.task('connect', function () {
    connect.server({
        root: 'src',
        livereload: true,
        port: 8081
    });
});

gulp.task('html', function () {
    gulp.src('./src/html/**/*.html')
        .pipe(connect.reload());
});

gulp.task('js', function () {
    gulp.src('./src/js/**/*.js')
        .pipe(connect.reload());
});

gulp.task('less', function () {
    gulp.src('./src/less/**/*.less')
        .pipe(less())
        .pipe(cleanCSS())
        .pipe(gulp.dest('src/css'))
        .pipe(connect.reload());
});

gulp.task('watch', function () {
    gulp.watch(['./src/html/**/*.html', './src/js/**/*.js', './src/less/**/*.less'], ['html', 'js', 'less']);
});


gulp.task('default', ['connect', 'watch']);

gulp.task('uglify', function () {

    gulp.src('./src/js/**/*.js')
        .pipe(uglify())
        .pipe(gulp.dest('./dist/js'));

    gulp.src('./src/less/**/*.less')
        .pipe(less())
        .pipe(cleanCSS())
        .pipe(gulp.dest('./dist/css'));

    gulp.src('./src/html/**/*.html')
        .pipe(htmlmin({collapseWhitespace: true}))
        .pipe(gulp.dest('./dist/html'))
});
