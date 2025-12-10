function init() {
    // Esta función está vacía en tu original, la dejamos así.
}

$(document).ready(function() {
    var usu_id = $('#user_idx').val(); // Lo toma del <input> en el header
    var rol_id = $('#rol_idx').val(); // Lo toma del <input> en el header

    /* Rutas de la API de Spring */
    var url_prefix;

    if (rol_id == 1) {
        // Si es Rol Usuario (ID=1), usamos las URLs de usuario
        // Estas URLs le pedirán al backend "dame los datos SÓLO de este usuario"
        url_prefix = "/api/dashboard/usuario/";
    } else {
        // Si es Admin (u otro rol), usamos las URLs de admin
        // Estas URLs le pedirán al backend "dame los datos de TODOS"
        url_prefix = "/api/dashboard/admin/";
    }

    /* --- Cargar Cajas de Estadísticas --- */

    // Total de Tickets
    // Reemplaza: ../../controller/usuario.php?op=total
    $.post(url_prefix + "total", { usu_id: usu_id }, function(data) {
        // Asumimos que Spring devolverá JSON directamente, no necesitamos JSON.parse()
        $('#lbltotal').html(data.TOTAL);
    });

    // Tickets Abiertos
    // Reemplaza: ../../controller/usuario.php?op=totalabierto
    $.post(url_prefix + "totalabierto", { usu_id: usu_id }, function(data) {
        $('#lbltotalabierto').html(data.TOTAL);
    });

    // Tickets Cerrados
    // Reemplaza: ../../controller/usuario.php?op=totalcerrado
    $.post(url_prefix + "totalcerrado", { usu_id: usu_id }, function(data) {
        $('#lbltotalcerrado').html(data.TOTAL);
    });

    /* --- Cargar Gráfico de Barras --- */
    // Reemplaza: ../../controller/usuario.php?op=grafico
    $.post(url_prefix + "grafico", { usu_id: usu_id }, function(data) {
        new Morris.Bar({
            element: 'divgrafico',
            data: data,
            xkey: 'nom',
            ykeys: ['total'],
            labels: ['Value'],
            barColors: ["#1AB244"],
        });
    });

    /* --- Cargar Calendario --- */
    var calendarEventsUrl;
    var calendarData = {};

    if (rol_id == 1) {
        // Reemplaza: ../../controller/ticket.php?op=usu_calendar
        calendarEventsUrl = '/api/dashboard/calendar/usuario';
        calendarData = { usu_id: usu_id }; // Enviamos el ID del usuario
    } else {
        // Reemplaza: ../../controller/ticket.php?op=all_calendar
        calendarEventsUrl = '/api/dashboard/calendar/admin';
    }

    $('#idcalendar').fullCalendar({
        lang: 'es',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,basicWeek,basicDay'
        },
        defaultView: 'month',
        events: {
            url: calendarEventsUrl,
            method: 'POST',
            data: calendarData
        }
    });

});

init();