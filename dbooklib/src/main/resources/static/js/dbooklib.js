$(function () {
    localMem = {
        isRead: true
    }

    meta = {
        host: 'http://' + window.location.host + '/dbooklib/'
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
        {return}
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
                $("li.card-author").html($content.author);
                $("p.card-text").html($content.intro);
                $("li.card-year").html($content.publish_year);
                $("img.card-img-top").attr({ "src": meta.host + 'pics/' + $content.image});
                $("#dlib-main-detail").removeAttr('hidden')
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

    var openNewWindow = () => {
        newWin = window.open('_blank')
        newWin.location = meta.host + 'read/new'
    }

    var openReadDetail = (id) => {
        newWin = window.open('_blank')
        newWin.location = meta.host + 'rest/read/' + id
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

    $("#dlib-header-filter-year-group-2019").on("click", { date: '2019' }, onClickYear);
    $("#dlib-header-filter-year-group-2018").on("click", { date: '2018' }, onClickYear);
    $("#dlib-header-filter-year-group-2017").on("click", { date: '2017' }, onClickYear);
    $("#dlib-header-filter-year-group-all").on("click", { date: '全部' }, onClickYear);

    $("#dlib-nav-category-all").on("click", { category: '全部' }, onClickCategory);
    $("#dlib-nav-category-history").on("click", { category: '历史' }, onClickCategory);
    $("#dlib-nav-category-literature").on("click", { category: '文学' }, onClickCategory);
    $("#dlib-nav-category-popularization").on("click", { category: '论述' }, onClickCategory);
    $("#dlib-nav-category-computer").on("click", { category: '计算机' }, onClickCategory);
    $("#dlib-nav-category-cartoon").on("click", { category: '漫画' }, onClickCategory);

    $('#dlib-header-filter-add-new').on('click', openNewWindow);
    $('#dlib-header-filter-local-batch').on('click', loadKindleLocally);

    for (var index = 1; index <= 10; index++) {
        $("#page_" + index).on("click", { pageNo: index }, onClickPage);
    }

});