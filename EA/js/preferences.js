const API = "http://" + window.location.hostname;

var updatePreferences,
    likePost,
    deletePreference,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    },
    updateCurrentAsync = (newPreference, newId) => {
        data = {
            "current": newPreference,
            "currentId": newId
        }
        fetch(API + ':3000/preferences/0', {
            headers: { "Content-type": "application/json" },
            method: 'PATCH',
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(json => console.log(json))
    }


async function getRecordsAsync(url) {
    let response = await fetch(url),
        data = await response.json()
    return data;
}



document.addEventListener('DOMContentLoaded', function () {

    var inputs = document.querySelectorAll('.input-field input[required]'),
        selElems = document.querySelectorAll('select');


    deletePreference = (id) => {
        fetch(API + ':3000/preferences/' + id, { method: 'DELETE' });
        var el = event.currentTarget.parentElement;
        el.parentElement.removeChild(el);

    };
    let i = 0, current;
    var addIcon = (element, item) => {
        let parent = item.parentElement;
        parent.className = 'd_fl a_c j_sb';
        //parent.innerHTML += `<i class="material-icons clickable" onclick="deletePreference('${element.id}')">delete</i>`
    }
    getRecordsAsync(API + ":3000/preferences")
        .then(path => {
            path.forEach(element => {
                if (element.current != undefined) {
                    current = element.current;
                    return;
                }
                let selected = '';
                element.name == current ? selected = 'selected' : '';
                selElems[0].innerHTML += `
                        <option data-id="${element.id}" ${selected} value="${element.name}">${element.name}</option>
                    `
                if (element.name == current) {
                    inputs[0].value = element.light;
                    inputs[1].value = element.temperature;
                    inputs[2].value = element.water;
                }
            });
            var selInstances = M.FormSelect.init(selElems);
            M.updateTextFields()
            let list = document.querySelectorAll(".dropdown-content span");
            for (let i = 0; i < path.length - 1; i++) {
                list[i].addEventListener("click", function () {
                    let currentName = this.innerText.replace('\ndelete', ''),
                        origin = document.querySelector('[value="' + currentName + '"]'),
                        dataId = origin.getAttribute('data-id');
                    updateCurrentAsync(currentName, dataId);
                    currentId = getRecordsAsync(API + ':3000/preferences/' + dataId)
                        .then(data => {
                            inputs[0].value = data.light;
                            inputs[1].value = data.temperature;
                            inputs[2].value = data.water;
                        });
                });
                if (path[i + 1].name != current)
                    addIcon(path[i + 1], list[i]);
            }
        });
    updatePreferences = () => {
        currentId = getRecordsAsync(API + ':3000/preferences/0')
            .then(data => {
                var record = new Object();
                record = {
                    "light": Number(inputs[0].value),
                    "temperature": Number(inputs[1].value),
                    "water": Number(inputs[2].value)
                }
                fetch(API + ':3000/preferences/' + data.currentId, {
                    headers: { 'Content-Type': 'application/json' },
                    method: "PATCH",
                    body: JSON.stringify(record)
                })
            })
    }

    var navElems = document.querySelectorAll('.sidenav'),
        navOptions = {
            "edge": "right"
        },
        navInstances = M.Sidenav.init(navElems, navOptions);


});