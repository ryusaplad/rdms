$(document).ready(function () {
    //yearSelect
    //courseSelect
    //semGradeSelect

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
            <option value="ABM">Accountancy and Business Management (ABM)</option>
<option value="HUMSS">Humanities and Social Sciences (HUMSS)</option>
<option value="STEM">Science, Technology, Engineering &amp; Mathematics (STEM)
</option>
<option value="gasncii">General Academic Strand</option>
<option value="sports">Sports</option>
<option value="bhcncii">Beauty/Hair Care (NCII)</option>
<option value="bppncii">Bread Pastry Production (NCII)</option>
<option value="cookeryncii">Cookery (NCII)</option>
<option value="dressmakingncii">Dressmaking (NCII)</option>
<option value="fbsncii">Food and Beverage Services (NCII)</option>
<option value="fosncii">Front Office Services (NCII)</option>
<option value="hairncii">Hairdressing (NCII)</option>
<option value="housencii">Housekeeping (NCII)</option>
<option value="lgsncii">Local Guiding Services (NCII)</option>
<option value="tailoringncii">Tailoring (NCII)</option>
<option value="tourismncii">Tourism Promotion Services (NCII)</option>
<option value="wellnessncii">Wellness Massage (NCII)</option>
<option value="animationncii">Animation (NCII)</option>
<option value="computerhardwarencii">Computer Hardware Servicing (NCII)</option>
<option value="comprogncii">Computer Programming (NCII)</option>
<option value="autoncii">Automotive Servicing (NCII)</option>
<option value="cesncii">Consumer Electronics Servicing (NCII)</option>
<option value="eincii">Electrical Installation Maintenance (NCII)</option>
<option value="isncii">International Subject (IS)</option>
            </select>
            <label for="course">Courses
                <span class="badge badge-secondary text-danger">*</span>
            </label>
        </div>`);
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
            <option value="BEC">Bachelor of Early Childhood</option>
            <option value="BEEd">Bachelor of Elementary Education (Gen. Ed.)</option>
            <option value="BPEd">Bachelor of Physical Education</option>
            <option value="BPA">Bachelor of Public Administration</option>
            <option value="BSA">Bachelor of Science in Accountancy</option>
            <option value="BSHM">Bachelor of Science in Hospitality Management</option>
            <option value="BSIT">Bachelor of Science in Information Technology</option>
            <option value="BSEdEng">Bachelor of Secondary Education (English)</option>
            <option value="BSEdFil">Bachelor of Secondary Education (Filipino)</option>
            <option value="BSEdMath">Bachelor of Secondary Education (Math)</option>
            <option value="BSEdSci">Bachelor of Secondary Education (Science)</option>
            <option value="BSEdSocSci">Bachelor of Secondary Education (Social Studies)</option>
            <option value="BSEdVal">Bachelor of Secondary Education (Values)</option>
            <option value="BTVTEd">Bachelor of Technical Vocational Teacher Education</option>
            </select>
            <label for="course">Courses
                <span class="badge badge-secondary text-danger">*</span>
            </label>
        </div>`);
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