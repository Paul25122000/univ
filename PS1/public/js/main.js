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
    const api = 'http://192.168.64.6/univ/PS1/public/api/preferences/';
    var items = document.querySelectorAll('.data .item b');
    getRecordsAsync(api)
        .then(data => {
            data.forEach(preference => {
                if (preference.selected) {
                    document.querySelector('#wall .heading span').innerHTML = preference.name;
                    items[0].innerHTML = preference.light;
                    items[1].innerHTML = preference.temperature;
                    items[2].innerHTML = preference.water;
                    M.updateTextFields();
                }
            });
        });

    var navElems = document.querySelectorAll('.sidenav')
    navOptions = {
        "edge": "right"
    },
        instances = M.Sidenav.init(navElems, navOptions);
});