var makePost, likePost, deletePost, deletePost,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    }

async function getRecordsAsync(url) {
    let response = await fetch(url),
        data = await response.json()
    return data;
}

document.addEventListener('DOMContentLoaded', function () {
    // const api = 'https://tonu.rocks/school/GreenHouse/api/';
    const api = 'http://192.168.64.6/public/api/preferences/';
    var items = document.querySelectorAll('.data .item b');
    getRecordsAsync(api)
        .then(data => {
            document.querySelector('#wall .heading span').innerHTML = data.current;
            items[0].innerHTML = data.light;
            items[1].innerHTML = data.temperature;
            items[2].innerHTML = data.water;
            M.updateTextFields();
        });

    var navElems = document.querySelectorAll('.sidenav')
        navOptions = {
            "edge": "right"
        },
        instances = M.Sidenav.init(navElems, navOptions);
});