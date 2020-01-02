$(function () {
    localMem = {
        isRead: true
    }

    meta = {
        host: 'http://' + window.location.host + '/dbooklib/'
        // host: 'http://' + 'localhost:8080' + '/dbooklib/'
    }

    var getBooksPost = (input) => {
        var url = meta.host + 'rest/books'

        $.ajax(url, {
            dataType: "html",
            method: "POST",
            data: JSON.stringify(input),
            headers: { "Content-Type": "application/json" },
            success: function (response) {
                var resJson = JSON.parse(response)
                $("#dlib-main-book-list li").remove()

                $.each(resJson, function (index, value) {
                    var $item = $('<li class="list-group-item"></li>')
                    var $name = $('<a></a>').addClass('d-inline').addClass('dlib-main-list-name').attr({ 'href': '#' })
                    $name.html(value.name)
                    $name.on("click", {id: value.bookReferId}, getDetail)
                    if (value.isNew) {
                        $name.removeClass('dlib-main-list-name').addClass('dlib-main-list-name-highlight')
                    }
                    var $detailLink = $('<a></a>').addClass('d-inline').addClass('dlib-main-list-dlink').attr({ 'href': meta.host + 'read/' + value._id })
                    $detailLink.html('详细')
                    var $type = $('<a></a>').addClass('d-inline').addClass('dlib-main-list-author').attr({ 'href': '#' })
                    $type.html(value.type)
                    var $isread = $('<a></a>').addClass('d-inline').addClass('dlib-main-list-isread')
                    if (value.year != 'TODO') {
                        $isread.html(value.year)
                    }

                    $item.append($name)
                    $item.append($detailLink)
                    $item.append($type)
                    $item.append($isread)

                    $("#dlib-main-book-list").append($item)
                })
            }
        });
    }

    var getDetail = (event) => {
        if (event.data.id === undefined)
        {
            $('.card').hide()
            return
        }
        var url = meta.host + 'rest/detail/' + event.data.id
        $.ajax(url, {
            dataType: "html",
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            success: function (response) {
                $content = JSON.parse(response);
                var $card = $(".card");
                $card.find("h5.dlib-main-detial-name").html($content.name);
                $("li.card-author").html($content.author!=null?$content.author:'作者信息未知');
                $("p.card-text").html($content.intro);
                $("li.card-year").html($content.publish_year);
                $("li.card-star").html($content.score + '  ' + '&#9734;&#9734;&#9734;&#9734;&#9734;');
                $("img.card-img-top").attr({"src": $content.image});
                $(".card-forward-link").on('click', {link: $content.link}, openNewWindowV3)
                $("#dlib-main-detail").removeAttr('hidden')
                $('.card').show()
            },
            error: function (request, errType, errMsg) {
                alert(errType);
            }
        })
    };

    var loadKindleLocally = () => {
        console.log("batch adding.")
        url = meta.host + 'rest/read/load'

        $.ajax(url, {
            dataType: "html",
            method: 'GET',
            headers: { "Content-Type": "application/json" },
            success: function (response) {
                var $data = JSON.parse(response)
                if ($data.isSuccessful) {
                    alert("Kindle电子书载入成功：" + $data.updatedNumber + "本")
                    $("#dlib-header-filter-isread-group-unread").click()
                }
                else {
                    alert("载入失败，数据更新：" + $data.updateSuccessful ? +"成功" : "失败。文件归档：" + $data.fileArchived ? "成功" : "失败")
                }
            }
        })
    }

    var onClickIsRead = (event) => {
        console.log('search by is read flag.')
        $("#dlib-header-filter-isread-group button").html(event.data.label)
        $("#dlib-header-filter-year-group button").html('全部')
        var request = {}

        localMem.isRead = event.data.isRead;
        localMem.date = null
        $.extend(true, request, localMem);
        getBooksPost(request);
    }

    var onClickPage = (event) => {
        console.log('search by is page.')
        var request = {}
        $.extend(true, request, localMem);
        request.pageNo = event.data.pageNo
        getBooksPost(request);
    }

    var onClickYear = (event) => {
        console.log('search by is year flag.')
        $("#dlib-header-filter-year-group button").html(event.data.date)
        $("#dlib-header-filter-isread-group button").html('已读')
        var request = {}
        if (event.data.date != '全部') {
            localMem.date = event.data.date
        }
        else {
            localMem.date = null
        }
        $.extend(true, request, localMem)
        getBooksPost(request)
    }

    var onClickCategory = (event) => {
        console.log('search by is category flag.')

        var request = {}
        if (event.data.category != '全部') {
            localMem.category = event.data.category
        }
        else {
            localMem.category = null
        }
        localMem.isRead = true;
        localMem.date = null;
        $.extend(true, request, localMem)
        getBooksPost(request)

        $("#dlib-header-filter-year-group button").html("全部")
        $("#dlib-header-filter-isread-group button").html('已读')

        $("#dlib-nav-category-group li.nav-item").removeClass('active')
    }

    var onBookSearch = () => {
        console.log('search from search box.')
        var toSearchVal = $("#dlib-header-search-input").val()
        var request = { 'toSearch': toSearchVal }
        $("#dlib-header-filter-isread-group button").html('全部')
        getBooksPost(request)
    }

    var openNewWindowV3 = (event) => {
        newWin = window.open('_blank')
        newWin.location = event.data.link
    }

    getBooksPost(localMem);

    $('#dlib-header-search-btn').on('click', onBookSearch)
    $('#dlib-header-search-input').keydown(function (event) {
        if (event.keyCode == 13) {
            console.log('search from search box.')
            var toSearchVal = $("#dlib-header-search-input").val()
            var request = { 'toSearch': toSearchVal }
            $("#dlib-header-filter-isread-group button").html('全部')
            getBooksPost(request)
        }
    })

    $("#dlib-header-filter-isread-group-read").on("click", { label: '已读', isRead: true }, onClickIsRead);
    $("#dlib-header-filter-isread-group-unread").on("click", { label: '未读', isRead: false }, onClickIsRead);
    $("#dlib-header-filter-isread-group-all").on("click", { label: '全部' }, onClickIsRead);

    $("#dlib-header-filter-year-group-2020").on("click", { date: '2020' }, onClickYear);
    $("#dlib-header-filter-year-group-2019").on("click", { date: '2019' }, onClickYear);
    $("#dlib-header-filter-year-group-2018").on("click", { date: '2018' }, onClickYear);
    $("#dlib-header-filter-year-group-2017").on("click", { date: '2017' }, onClickYear);
    $("#dlib-header-filter-year-group-2016").on("click", { date: '2016' }, onClickYear);
    $("#dlib-header-filter-year-group-2015").on("click", { date: '2015' }, onClickYear);
    $("#dlib-header-filter-year-group-2014").on("click", { date: '2014' }, onClickYear);
    $("#dlib-header-filter-year-group-2013").on("click", { date: '2013' }, onClickYear);
    $("#dlib-header-filter-year-group-2012").on("click", { date: '2012' }, onClickYear);
    $("#dlib-header-filter-year-group-2011").on("click", { date: '2011' }, onClickYear);
    $("#dlib-header-filter-year-group-2010").on("click", { date: '2010' }, onClickYear);
    $("#dlib-header-filter-year-group-2009").on("click", { date: '2009' }, onClickYear);
    $("#dlib-header-filter-year-group-2008").on("click", { date: '2008' }, onClickYear);
    $("#dlib-header-filter-year-group-2007").on("click", { date: '2007' }, onClickYear);
    $("#dlib-header-filter-year-group-2006").on("click", { date: '2006' }, onClickYear);
    $("#dlib-header-filter-year-group-2005").on("click", { date: '2005' }, onClickYear);
    $("#dlib-header-filter-year-group-2004").on("click", { date: '2004' }, onClickYear);
    $("#dlib-header-filter-year-group-2003").on("click", { date: '2003' }, onClickYear);
    $("#dlib-header-filter-year-group-2002").on("click", { date: '2002' }, onClickYear);
    $("#dlib-header-filter-year-group-2001").on("click", { date: '2001' }, onClickYear);
    $("#dlib-header-filter-year-group-all").on("click", { date: '全部' }, onClickYear);

    $("#dlib-nav-category-all").on("click", { category: '全部' }, onClickCategory);
    $("#dlib-nav-category-history").on("click", { category: '历史' }, onClickCategory);
    $("#dlib-nav-category-literature").on("click", { category: '文学' }, onClickCategory);
    $("#dlib-nav-category-popularization").on("click", { category: '论述' }, onClickCategory);
    $("#dlib-nav-category-computer").on("click", { category: '计算机' }, onClickCategory);
    $("#dlib-nav-category-cartoon").on("click", { category: '漫画' }, onClickCategory);
    $("#dlib-nav-category-analysis").on("click", { link: meta.host + 'analysis' }, openNewWindowV3);

    $('#dlib-header-filter-add-new').on('click', {link: meta.host + 'read/new'}, openNewWindowV3);
    $('#dlib-header-filter-local-batch').on('click', loadKindleLocally);

    for (var index = 1; index <= 10; index++) {
        $("#page_" + index).on("click", { pageNo: index }, onClickPage);
    }

});