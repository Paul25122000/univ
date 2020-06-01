var updateLogsView, makeLog, likeLog, deleteLog, deleteLog, setFilter,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    }

// api = 'https://tonu.rocks/school/GreenHouse/api/logs/';
const api = 'http://192.168.64.6/univ/PS1/public/api/logs/';

var logs = []
var lastLogId;
var filter = undefined;

async function getRecordsAsync(url) {
    let response = await fetch(url),
        data = await response.json()
    return data;
}

const updateLogsCollection = (data, container, type, isOldLog) => {
    if (!isOldLog && lastLogId != data?.id)
        logs.push(data)
    let row =
        `<tr class="log">
            <td>${data.name}</td>
            <td class="date_cell">${data.timestamp.split(" ")[0]}</td>
            <td>${data.timestamp.split(" ")[1]}</td>`;
    switch (type) {
        case 'temperature':
            row += `
            <td>${data.temperatureValue}</td>
            <td>${data.temperatureSet}</td>`
            break;
        case 'water':
            row += `
            <td>${data.waterValue}</td>
            <td>${data.waterSet}</td>`
            break;
        default:
            row += `
            <td>${data.lightValue}</td>
            <td>${data.lightSet}</td>`
            break;
    }
    row += `
        <td><i class="material-icons clickable" onclick="deleteLog('${data.id}')">delete</i><td>
        </tr>`
    if (filter === undefined || new Date(data.timestamp) >= filter)
        container.innerHTML += row;
    if (data?.id > lastLogId || lastLogId == undefined)
        lastLogId = data?.id
}

const updateRoutine = (container, type, autoReload) => {
    setInterval(() => {
        if (autoReload?.checked) {
            getRecordsAsync(api + "?limit=1")
                .then(record => {
                    record.forEach(log => {
                        // while new records are inserted -> update logs array
                        if (lastLogId != log?.id) {
                            // remove first log when array size becomes bigger than 10
                            if (logs.length == 10) {
                                logs.shift()
                                let firstLog = container.querySelector(".log");
                                container.removeChild(firstLog);
                            }
                            updateLogsCollection(log, container, type, false)
                        }
                    })
                })
        }
    }, 3000)
}

document.addEventListener('DOMContentLoaded', function () {
    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const currentDatetime = new Date();
    const formattedDate = months[currentDatetime.getMonth()] + ' ' + currentDatetime.getDate() + ", " + currentDatetime.getFullYear();


    const logsContainer = document.querySelector("#logs tbody");
    const renderLogs = (records) => {
        records.forEach(element => {
            updateLogsCollection(element, logsContainer, null, false)
        });
    }

    const getActiveTab = () => {
        var tab = document.querySelector('.tabs .active');
        return tab.innerHTML.toLowerCase();
    }

    const onTabChange = (tabName) => {
        logsContainer.innerHTML = '';
        logs.forEach(log => {
            updateLogsCollection(log, logsContainer, tabName.toLowerCase(), true)
        });
    }

    const autoReloadSwitch = document.querySelector(".switch input");
    getRecordsAsync(api)
        .then(data => {
            renderLogs(data)
            updateRoutine(logsContainer, null, autoReloadSwitch);
        })

    deleteLog = (id) => {
        logs.forEach((log, index) => {
            if (log?.id == id)
                logs.splice(index, 1)
        })
        logsContainer.removeChild(nearest("log", event.target));
    }

    const deleteAll = document.querySelector("#deleteAll");
    deleteAll.onclick = async () => {
        try {
            const response = await
                fetch(api + "?deleteAll=1", { method: "DELETE" })
            if (response.ok) {
                const text = await response.json();
                logsContainer.innerHTML = '';
                M.toast({ html: text, classes: 'indigo_toast' })
            }
        } catch (err) {
            M.toast({ html: 'Something went wrong', classes: 'indigo_toast' })
            console.error(err)
        }
    }

    setFilter = () => {
        let date = this.querySelector(".datepicker").value,
            time = this.querySelector(".timepicker").value;
        if (date != "" && time != "") {
            autoReloadSwitch.checked = false;
            logsContainer.innerHTML = '';
            filter = new Date(date + " " + time);
            const type = getActiveTab()
            logs.forEach(log => {
                updateLogsCollection(log, logsContainer, type, true)
            });
        }
        else {
            M.Datepicker.getInstance(datepicker[0]).open();
            M.toast({ html: 'Please select date and time', classes: 'indigo_toast' })
        }
    }

    const clearFilter = document.querySelector("#clearFilter")
    clearFilter.onclick = () => {
        filter = undefined;
        autoReloadSwitch.checked = true
        logsContainer.innerHTML = ''
        const type = getActiveTab()
        console.log(type)
        logs.forEach(log => {
            updateLogsCollection(log, logsContainer, type, true)
        })
    }

    const navElems = document.querySelectorAll('.sidenav'),
        tabElems = document.querySelectorAll('.tabs'),
        navOptions = {
            "edge": "right"
        },
        tabOptions = {
            "onShow": function () {
                let tabName = this.$activeTabLink[0].hash.replace('#', '');
                onTabChange(tabName)
            }
        },
        datepicker = document.querySelectorAll('.datepicker'),
        timepicker = document.querySelectorAll('.timepicker'),
        currentDate = new Date(),
        currentYear = currentDate.getFullYear(),
        dateOptions = {
            "maxDate": currentDate,
            "yearRange": [currentYear - 5, currentYear],
            "onClose": () => { M.Timepicker.getInstance(timepicker[0]).open() }
        },
        timeOptions = {
            "twelveHour": false
        },
        modlas = document.querySelectorAll('.modal');

    M.Sidenav.init(navElems, navOptions);
    M.Datepicker.init(datepicker, dateOptions);
    M.Timepicker.init(timepicker, timeOptions);
    M.Tabs.init(tabElems, tabOptions);
    M.Modal.init(modlas);

});