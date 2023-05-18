$(document).ready(function () {


    $("#schoolLevel").on("change", function (e) {
        const schoolLevel = $("#schoolLevel").val();
        var yearSelectHtml = $(".yearSelect");
        var courseSelectHtml = $(".courseSelect");
        var semGradeSelectHtml = $(".semGradeSelect");
        if (schoolLevel == "kinder") {

            yearSelectHtml.show();

            yearSelectHtml.empty();
            courseSelectHtml.empty();
            semGradeSelectHtml.empty();

            yearSelectHtml.append(` <select id="year" name="year" class="form-select" placeholder="Year Level" required>
            <option value="kinder1">Kinder Garten 1</option>
            <option value="kinder2">Kinder Garten 2</option>
        </select>
        <label for="year">Kinder
            <span class="badge badge-secondary text-danger ">*</span>`);

        } else if (schoolLevel == "elementary") {
            yearSelectHtml.show();
            courseSelectHtml.show();
            semGradeSelectHtml.show();

            yearSelectHtml.empty();
            courseSelectHtml.empty();
            semGradeSelectHtml.empty();

            yearSelectHtml.append(` <select id="year" name="year" class="form-select" placeholder="Year Level" required>
            <option value="grade1">Grade 1</option>
            <option value="grade2">Grade 2</option>
            <option value="grade3">Grade 3</option>
            <option value="grade4">Grade 4</option>
            <option value="grade5">Grade 5</option>
            <option value="grade6">Grade 6</option>

        </select>
        <label for="year">Elementary Level
            <span class="badge badge-secondary text-danger ">*</span>`);
        } else if (schoolLevel == "junior-high") {
            yearSelectHtml.show();

            yearSelectHtml.empty();
            courseSelectHtml.empty();
            semGradeSelectHtml.empty();

            yearSelectHtml.append(` <select id="year" name="year" class="form-select" placeholder="Year Level" required>
            <option value="grade7">Grade 7</option>
            <option value="grade8">Grade 8</option>
            <option value="grade9">Grade 9</option>
            <option value="grade10">Grade 10</option>
        </select>
        <label for="year">Junior Level
            <span class="badge badge-secondary text-danger ">*</span>`);
        } else if (schoolLevel == "senior-high") {
            yearSelectHtml.show();
            courseSelectHtml.show();


            yearSelectHtml.empty();
            courseSelectHtml.empty();
            semGradeSelectHtml.empty();
            yearSelectHtml.append(` <select id="year" name="year" class="form-select" placeholder="Year Level" required>
            <option value="grade11">Grade 11</option>
            <option value="grade12">Grade 12</option>
        </select>
        <label for="year">Senior High Level
            <span class="badge badge-secondary text-danger ">*</span>`);


            courseSelectHtml.append(`<div class="mt-2 form-floating">
            <select id="course" name="course" class="form-select" required>
           
            </select>
            <label for="course">Courses
                <span class="badge badge-secondary text-danger">*</span>
            </label>
        </div>`);

            $.ajax({
                url: `/student/load/school-program-informations/shs/available`,
                method: "GET",
                success: function (response) {


                    var options = response.map(function (item) {
                        return '<option value="' + item.code + '">' + item.name + '</option>';
                    });


                    $('#course').append(options);
                },
                error: function () {
                    console.log('Error occurred during AJAX request.');
                }
            });

        } else if (schoolLevel == "college") {
            yearSelectHtml.show();
            courseSelectHtml.show();
            semGradeSelectHtml.show();

            yearSelectHtml.empty();
            courseSelectHtml.empty();
            semGradeSelectHtml.empty();

            yearSelectHtml.append(` <select id="year" name="year" class="form-select" placeholder="Year Level" required>
            <option value="1ST YEAR">1ST YEAR</option>
            <option value="2ND YEAR">2ND YEAR</option>
            <option value="3RD YEAR">3RD YEAR</option>
            <option value="4TH YEAR">4TH YEAR</option>
            <option value="5TH YEAR">5TH YEAR</option>
        </select>
        <label for="year">Year
            <span class="badge badge-secondary text-danger ">*</span>`);

            semGradeSelectHtml.append(`<select id="semester" name="semester" class="form-select" required>
            <option value="1ST SEMESTER">1ST SEMESTER</option>
            <option value="2ND SEMESTER">2ND SEMESTER</option>
        </select>
        <label for="semester">Semester
            <span class="badge badge-secondary text-danger ">*</span>`);

            courseSelectHtml.append(`<div class="mt-2 form-floating">
            <select id="course" name="course" class="form-select" required>
            </select>
            <label for="course">Courses
                <span class="badge badge-secondary text-danger">*</span>
            </label>
        </div>`);

            $.ajax({
                url: `/student/load/school-program-informations/college/available`,
                method: "GET",
                success: function (response) {

                    var options = response.map(function (item) {
                        return '<option value="' + item.code + '">' + item.name + '</option>';
                    });

                    $('#course').append(options);
                },
                error: function () {
                    console.log('Error occurred during AJAX request.');
                }
            });
        } else {
            yearSelectHtml.hide();
            courseSelectHtml.hide();
            semGradeSelectHtml.hide();

            yearSelectHtml.empty();
            courseSelectHtml.empty();
            semGradeSelectHtml.empty();
        }
    });
});