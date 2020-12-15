<template>
  <div>
    <v-tabs
      v-model="tab"
      centered
      icons-and-text
      dark
      background-color="#3FA7DB"
      show-arrows
      color="white"
    >
      <v-tab href="#graficos" @click="bloquearbtn()">
        Generar Reportes Graficos
        <v-icon left> mdi-chart-bar </v-icon>
      </v-tab>
      <v-tab href="#historial" @click="bloquearbtn()">
        Generar Historial Clinico
        <v-icon left> mdi-calendar-account-outline </v-icon>
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item
        :key="2"
        :value="'graficos'"
        class="spacing-playground pa-8"
        :style="[
          $vuetify.theme.dark
            ? { background: '#121212' }
            : { background: 'white' }
        ]"
      >
        <v-card
          flat
          class="mx-auto my-0"
          max-width="600"
          :style="[
            $vuetify.theme.dark
              ? { background: '#121212' }
              : { background: 'white' }
          ]"
        >
          <div>
            <h2 align="center" justify="center">Datos generales</h2>
            <v-select
              v-model="tipoReporte"
              :items="tiposdeReporte"
              class="ma-3"
              justify="center"
              align="center"
              label="Tipo de Reporte"
              placeholder="Seleccione un tipo de reporte"
              outlined
              dense
              :menu-props="{ bottom: true, offsetY: true }"
              @change="getPagos()"
              v-if="user.role == 'ADMIN' || user.role == 'Dentista'"
            >
            </v-select>

            <v-select
              v-model="consultorio"
              :items="consultorios"
              class="ma-3"
              justify="center"
              name="nombre"
              align="center"
              item-text="nombre"
              return-object
              label="Consultorio"
              placeholder="Seleccione un consultorio"
              outlined
              dense
              :disabled="banderaConsultorio"
              :menu-props="{ bottom: true, offsetY: true }"
              @change="obtenerDentistasxConsultorio()"
              v-if="
                (user.role == 'ADMIN' || user.role == 'Dentista') &&
                  (tipoReporte == 'Ingresos' || tipoReporte == 'Gastos')
              "
            >
            </v-select>
            <img
              style="display: none"
              width="592"
              height="42"
              id="img"
              src="../assets/membrete.png"
            />

            <v-select
              v-model="dentista"
              :items="listaDentistas"
              class="ma-3"
              justify="center"
              item-value="nombre"
              align="center"
              item-text="nombre"
              return-object
              label="Dentista"
              :disabled="banderaDentista"
              placeholder="Seleccione un dentista"
              outlined
              dense
              :menu-props="{ bottom: true, offsetY: true }"
              v-if="
                (user.role == 'ADMIN' || user.role == 'Dentista') &&
                  tipoReporte == 'Ingresos'
              "
            >
            </v-select>

            <h2
              align="center"
              justify="center"
              v-if="tipoReporte == 'Ingresos' || tipoReporte == 'Gastos'"
            >
              Rango de fechas
            </h2>

            <v-row>
              <v-col cols="6" sm="6">
                <v-menu
                  ref="menu1"
                  v-model="menu1"
                  :close-on-content-click="false"
                  :return-value.sync="fecha1"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                  v-if="tipoReporte == 'Ingresos' || tipoReporte == 'Gastos'"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-combobox
                      v-model="fecha1"
                      chips
                      small-chips
                      label="Fecha Inicial"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-combobox>
                  </template>
                  <v-date-picker v-model="fecha1" no-title scrollable>
                    <v-spacer></v-spacer>
                    <v-btn text color="primary" @click="menu1 = false">
                      Cancelar
                    </v-btn>
                    <v-btn
                      text
                      color="primary"
                      @click="$refs.menu1.save(fecha1)"
                    >
                      OK
                    </v-btn>
                  </v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="6" sm="6">
                <v-menu
                  ref="menu2"
                  v-model="menu2"
                  class="ma-3"
                  :close-on-content-click="false"
                  :return-value.sync="fecha2"
                  transition="scale-transition"
                  offset-y
                  v-if="tipoReporte == 'Ingresos' || tipoReporte == 'Gastos'"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-combobox
                      v-model="fecha2"
                      chips
                      small-chips
                      label="Fecha Final"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-combobox>
                  </template>
                  <v-date-picker v-model="fecha2" no-title scrollable>
                    <v-spacer></v-spacer>
                    <v-btn text color="primary" @click="menu2 = false">
                      Cancelar
                    </v-btn>
                    <v-btn
                      text
                      color="primary"
                      @click="$refs.menu2.save(fecha2)"
                    >
                      OK
                    </v-btn>
                  </v-date-picker>
                </v-menu>
              </v-col>
            </v-row>
            <!--             <v-btn @click="metodoPDF()"> boton prueba </v-btn>
 -->

            <v-btn
              color="primary"
              @click="ObtenerDatos()"
              block
              :disabled="banderabton"
            >
              Generar PDF
            </v-btn>

            <h2 v-show="gastosaux" class="my-10" align="center">
              Gastos por mes
            </h2>
            <h2 v-show="ingresosaux" class="my-10" align="center">
              Ingresos por mes
            </h2>

            <canvas
              v-show="vergraficas"
              id="MiGrafica"
              width="500"
              height="280"
            ></canvas>
            <h2 v-show="gastosaux" class="my-10" align="center">
              Gastos por Año
            </h2>
            <h2 v-show="ingresosaux" class="my-10" align="center">
              Ingresos por Año
            </h2>

            <canvas
              v-show="vergraficas"
              id="MiGrafica2"
              width="500"
              height="280"
            ></canvas>
            <h2 v-show="gastosaux" class="my-10" align="center">
              Productos con más gastos
            </h2>
            <h2 v-show="ingresosaux" class="my-10" align="center">
              Productos con más ingresos
            </h2>

            <canvas
              v-show="vergraficas"
              id="MiGrafica3"
              width="500"
              height="280"
            ></canvas>
          </div>
        </v-card>
      </v-tab-item>
      <v-tab-item
        :key="1"
        :value="'historial'"
        class="spacing-playground pa-8"
        :style="[
          $vuetify.theme.dark
            ? { background: '#121212' }
            : { background: 'white' }
        ]"
      >
        <v-card
          flat
          class="mx-auto my-0"
          max-width="600"
          :style="[
            $vuetify.theme.dark
              ? { background: '#121212' }
              : { background: 'white' }
          ]"
        >
          <h2 align="center" justify="center">Datos generales</h2>

          <v-select
            v-model="auxTipo"
            :items="reporteClinico"
            class="ma-3"
            justify="center"
            align="center"
            label="Tipo de Reporte"
            placeholder="Seleccione un tipo de reporte"
            outlined
            dense
            @change="getPagos()"
            :menu-props="{ bottom: true, offsetY: true }"
            v-if="user.role == 'ADMIN' || user.role == 'Dentista'"
          >
          </v-select>

          <v-select
            v-model="pacienteModel"
            :items="pacientes"
            class="ma-3"
            justify="center"
            name="nombre"
            align="center"
            item-text="nombre"
            return-object
            label="Paciente"
            placeholder="Seleccione un Paciente"
            outlined
            dense
            :menu-props="{ bottom: true, offsetY: true }"
            v-if="auxTipo == 'Historial Clinico'"
          >
          </v-select>

          <h2
            align="center"
            justify="center"
            v-if="auxTipo == 'Historial Clinico'"
          >
            Rango de fechas
          </h2>

          <v-row>
            <v-col cols="6" sm="6">
              <v-menu
                ref="menu3"
                v-model="menu3"
                :close-on-content-click="false"
                :return-value.sync="fecha3"
                transition="scale-transition"
                offset-y
                min-width="290px"
                v-if="auxTipo == 'Historial Clinico'"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-combobox
                    v-model="fecha3"
                    chips
                    small-chips
                    label="Fecha Inicial"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                  ></v-combobox>
                </template>
                <v-date-picker v-model="fecha3" no-title scrollable>
                  <v-spacer></v-spacer>
                  <v-btn text color="primary" @click="menu3 = false">
                    Cancelar
                  </v-btn>
                  <v-btn text color="primary" @click="$refs.menu3.save(fecha3)">
                    OK
                  </v-btn>
                </v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" sm="6">
              <v-menu
                ref="menu4"
                v-model="menu4"
                class="ma-3"
                :close-on-content-click="false"
                :return-value.sync="fecha4"
                transition="scale-transition"
                offset-y
                v-if="auxTipo == 'Historial Clinico'"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-combobox
                    v-model="fecha4"
                    chips
                    small-chips
                    label="Fecha Final"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                  ></v-combobox>
                </template>
                <v-date-picker v-model="fecha4" no-title scrollable>
                  <v-spacer></v-spacer>
                  <v-btn text color="primary" @click="menu4 = false">
                    Cancelar
                  </v-btn>
                  <v-btn text color="primary" @click="$refs.menu4.save(fecha4)">
                    OK
                  </v-btn>
                </v-date-picker>
              </v-menu>
            </v-col>
          </v-row>

          <v-btn
            color="primary"
            @click="ObtenerDatos()"
            block
            :disabled="banderabton"
          >
            Generar Historial Clinico
          </v-btn>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import { environment } from "../env/environment";
import Chart from "chart.js";

export default {
  data: () => ({
    url: environment.url + "Usuario",
    dialog: false,
    tab: null,
    vergraficas: false,
    gastosaux: false,
    ingresosaux: false,
    graficaAño: null,
    consultorios: [],
    citasxdentista: [],
    banderaConsultorio: false,
    banderaDentista: false,
    consultorio: null,
    historialPaciente: [],
    tratamientoPaciente: [],
    pacienteModel: null,
    pacientes: [],
    banderabton: true,
    gastosTotales: 0,
    auxTipo: null,
    reporteClinico: ["Historial Clinico"],
    ingresosTotale: 0,
    auxProductosGastos: [],
    iniciofecha: null,
    finalfecha: null,
    gastosxMes: [
      { Mes: 1, gastos: 0 },
      { Mes: 2, gastos: 0 },
      { Mes: 3, gastos: 0 },
      { Mes: 4, gastos: 0 },
      { Mes: 5, gastos: 0 },
      { Mes: 6, gastos: 0 },
      { Mes: 7, gastos: 0 },
      { Mes: 8, gastos: 0 },
      { Mes: 9, gastos: 0 },
      { Mes: 10, gastos: 0 },
      { Mes: 11, gastos: 0 },
      { Mes: 12, gastos: 0 }
    ],
    gastosxYear: [],
    productosMasGastos: [],
    serviciosMasIngresos: [],
    dentistas: [],
    dentista: null,
    listaDentistas: [],
    tipoReporte: null,
    tiposdeReporte: ["Gastos", "Ingresos"],
    menu1: null,
    menu2: null,
    menu3: null,
    menu4: null,
    fecha1: null,
    fecha2: null,
    fecha3: null,
    fecha4: null,
    name: "Pdfgen"
  }),

  computed: {
    ...mapState(["user"])
  },

  watch: {
    dialog(val) {
      if (!val) {
        this.close();
        this.$refs.form.reset();
      }
    },
    dialogDelete(val) {
      val || this.closeDelete();
    }
  },
  created() {
    this.initialize();
  },
  methods: {
    initialize() {
      this.getConsultorios();
      this.getDentistas();
      this.getPacientes();
      if (this.user.role == "Dentista") {
        this.consultorio = this.user.consultorio;
      }
    },

    getConsultorios() {
      this.$vloading.show();
      axios
        .get(environment.url + "Consultorio")
        .then(response => {
          this.consultorios = response.data;

          if (this.consultorios.length == 0)
            this.$toast.info("No hay registros consultorios");

          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los consultorios");
        });
    },

    getDentistas() {
      this.$vloading.show();
      axios
        .get(environment.url + "Usuario/role/Dentista")
        .then(response => {
          this.dentistas = response.data;

          if (this.consultorios.length == 0)
            this.$toast.info("No hay registros de dentistas");

          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los dentistas");
        });
    },
    getPacientes() {
      this.$vloading.show();
      axios
        .get(environment.url + "Usuario/role/Paciente")
        .then(response => {
          this.pacientes = response.data;

          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los pacientes");
        });
    },

    bloquearbtn() {
      this.banderabton = true;
      this.consultorio = null;
      this.tipoReporte = null;
      this.dentista = null;
      this.auxTipo = null;
      this.fecha1 = null;
      this.fecha2 = null;
      this.fecha3 = null;
      this.fecha4 = null;
      this.menu1 = null;
      this.menu2 = null;
      this.menu3 = null;
      this.menu4 = null;
      this.vergraficas = false;
      this.ingresosaux = false;
      this.gastosaux = false;
    },
    auxMetodo() {
      console.log(this.pacienteModel);
      axios
        .get(environment.url + "Cita/Paciente/" + this.pacienteModel.id)
        .then(response => {
          console.log(response.data);
        })
        .catch(() => {});
    },

    getPagos() {
      this.banderabton = false;
      this.consultorio = null;
      this.dentista = null;
      this.fecha1 = null;
      this.fecha2 = null;
      this.fecha3 = null;
      this.fecha4 = null;
      this.menu1 = null;
      this.menu2 = null;
      this.menu3 = null;
      this.menu4 = null;
      this.vergraficas = false;
      this.ingresosaux = false;
      this.gastosaux = false;

      if (this.user.role == "Dentista") {
        this.consultorios.forEach(uwu => {
          if (uwu.id == this.user.consultorio) {
            this.consultorio = uwu;
            this.banderaConsultorio = true;
          }
        });

        this.dentistas.forEach(uwu => {
          if (uwu.id == this.user.id) {
            console.log("entra en 537");
            this.listaDentistas.push(uwu);
            this.dentista = uwu;
            this.banderaDentista = true;
          }
        });
        console.log(this.dentista);
      }
      if (this.tipoReporte == "Ingresos") {
        console.log("e");
      }
    },

    obtenerDentistasxConsultorio() {
      var auxDentistas = [];
      this.listaDentistas = [];
      this.dentistas.forEach(uwu => {
        if (uwu.datosConsultorio.nombre == this.consultorio.nombre)
          auxDentistas.push(uwu);
      });

      this.listaDentistas = auxDentistas;
    },

    getBase64Image(img) {
      var canvas = document.createElement("canvas");
      canvas.width = img.width;
      canvas.height = img.height;
      var ctx = canvas.getContext("2d");
      ctx.drawImage(img, 0, 0);
      var dataURL = canvas.toDataURL();
      return dataURL;
    },

    getBase64Grafica(img) {
      var canvas = document.createElement("canvas");
      canvas.width = img.width;
      canvas.height = img.height;
      var ctx = canvas.getContext("2d");
      ctx.drawImage(img, 0, 0, img.width, img.height);
      var dataURL = canvas.toDataURL();

      return dataURL;
    },

    hoyFecha() {
      var hoy = new Date();
      var dd = hoy.getDate();
      var mm = hoy.getMonth() + 1;
      var yyyy = hoy.getFullYear();
      return dd + "/" + mm + "/" + yyyy;
    },

    obtenerFecha(fecha) {
      var dd = fecha.getDate();
      var mm = fecha.getMonth() + 1;
      var yyyy = fecha.getFullYear();
      return (
        dd +
        "/" +
        mm +
        "/" +
        yyyy +
        "       " +
        fecha.getHours() +
        ":" +
        fecha.getMinutes() +
        ":" +
        fecha.getSeconds()
      );
    },
    pdfgastosgen: function() {
      var hoy = new Date();
      var reportetipo = this.tipoReporte;
      reportetipo = reportetipo.toUpperCase();
      var nombreconsultorio = this.consultorio.nombre;
      var fecha = this.hoyFecha();
      var base64 = this.getBase64Image(document.getElementById("img"));
      var grafica1 = this.getBase64Grafica(
        document.getElementById("MiGrafica")
      );
      var grafica2 = this.getBase64Grafica(
        document.getElementById("MiGrafica2")
      );
      var grafica3 = this.getBase64Grafica(
        document.getElementById("MiGrafica3")
      );

      /*             console.log(grafica2);
       */
      var hora =
        hoy.getHours() + ":" + hoy.getMinutes() + ":" + hoy.getSeconds();

      var fechainicio = this.fecha1 + "";

      var iniciodia = parseInt(fechainicio.substring(8));
      var iniciomes = parseInt(fechainicio.substring(5, 7));
      var inicioaño = parseInt(fechainicio.substring(0, 4));

      var fechafinal = this.fecha2 + "";
      var finaldia = parseInt(fechafinal.substring(8));
      var finalmes = parseInt(fechafinal.substring(5, 7));
      var finalaño = parseInt(fechafinal.substring(0, 4));

      fechainicio = iniciodia + "/" + iniciomes + "/" + inicioaño;
      fechafinal = finaldia + "/" + finalmes + "/" + finalaño;

      var tabla = [];

      var column = [];
      column.push({
        text: "Fecha y hora",
        width: 150,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Producto",
        width: 150,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Recibió",
        width: 150,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Precio por unidad",
        width: 150,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Cantidad",
        width: 150,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Total",
        width: 150,
        border: [false, true, true, true],
        bold: true,
        style: "tableHeader",
        fillColor: "skyblue"
      });
      tabla.push(column);

      this.auxProductosGastos.forEach(element => {
        element.productosInventario.forEach(x => {
          if (x.cantidadActualizada > x.cantidadAnterior) {
            column = [];

            var ms = Date.parse(element.fechaModificacion);
            var fecha2 = new Date(ms);

            var auxfechaaux = fecha2;
            fecha2 = this.obtenerFecha(fecha2);

            if (
              auxfechaaux >= this.iniciofecha &&
              auxfechaaux <= this.finalfecha
            ) {
              column.push({ text: fecha2, style: "tableHeader" });
              column.push({ text: x.nombre, style: "tableHeader" });
              column.push({
                text: element.usuario.nombreUsuario,
                style: "tableHeader"
              });
              column.push({ text: x.precio, style: "tableHeader" });
              column.push({
                text: x.cantidadActualizada - x.cantidadAnterior,
                style: "tableHeader"
              });
              column.push({
                text:
                  "$" + (x.cantidadActualizada - x.cantidadAnterior) * x.precio,
                style: "tableHeader"
              });
              tabla.push(column);
            }
          }
        });
      });

      var pdfMake = require("pdfmake/build/pdfmake.js");
      if (pdfMake.vfs == undefined) {
        var pdfFonts = require("pdfmake/build/vfs_fonts.js");
        pdfMake.vfs = pdfFonts.pdfMake.vfs;
      }
      var docDefinition = {
        pageSize: "LETTER",
        pageMargins: [25, 130, 25, 35],

        header: function(currentPage, pageCount) {
          return [
            //margin : [ izquierda, arriba, derecha, abajo ]

            {
              image: base64,
              style: "subheader",
              margin: [10, 10, 0, 0],
              alignment: "center"
            },

            {
              fontSize: 10,
              columns: [
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "left",
                  bold: true
                },
                {
                  text: "REPORTE GRAFICO DE " + reportetipo,
                  width: 400,
                  margin: [10, 5, 0, 0],
                  alignment: "center",
                  bold: true,
                  fontSize: 15
                },
                {
                  text:
                    "Página: " + currentPage.toString() + " de " + pageCount,
                  margin: [10, 10, 20, 0],
                  alignment: "right"
                }
              ]
            },

            {
              fontSize: 10,
              columns: [
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "left",
                  bold: true
                },
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Fecha: " + fecha,
                  margin: [10, 5, 20, 0],
                  alignment: "right"
                }
              ]
            },
            {
              fontSize: 10,
              columns: [
                {
                  fontSize: 12,
                  text: "Consultorio: " + nombreconsultorio,
                  margin: [20, 5, 0, 0],
                  width: 170,
                  alignment: "left",
                  bold: true
                },
                {
                  fontSize: 12,
                  text: "Rango de fechas: " + fechainicio + " - " + fechafinal,
                  margin: [10, 5, 0, 0],
                  width: 300,
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Hora: " + hora,
                  margin: [10, 5, 20, 0],
                  alignment: "right"
                }
              ]
            }
          ];
        },
        content: [
          {},
          {
            text: "Compras Registradas",
            width: 400,
            margin: [10, 5, 0, 10],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            table: {
              // Defining the top 2 rows as the "sticky" header rows
              headerRows: 1,
              // One column, full width
              widths: ["16.6%", "16.6%", "16.6%", "16.6%", "16.6%", "16.6%"],
              body: tabla,

              layout: "noBorders"
            }
          },

          {
            fontSize: 12,
            text: "Total: $" + this.gastosTotales,
            height: 10,
            width: 100,
            margin: [470, 10, 0, 0]
          },

          {
            text: "Gastos por mes",
            width: 400,
            margin: [10, 5, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            image: grafica1,
            style: "subheader",
            margin: [10, 10, 0, 0],
            alignment: "center"
          },
          {
            text: "Gastos por año",
            width: 400,
            margin: [10, 5, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            image: grafica2,
            style: "subheader",
            margin: [10, 10, 0, 0],
            alignment: "center"
          },
          {
            text: "Productos con más gastos producidos",
            width: 400,
            margin: [10, 5, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            image: grafica3,
            style: "subheader",
            margin: [10, 10, 0, 0],
            alignment: "center"
          }
        ],

        footer: function() {
          return [
            {
              canvas: [
                {
                  type: "line",
                  color: [0, 0, 0],
                  x1: 10,
                  y1: 10,
                  x2: 590,
                  y2: 10,
                  lineWidth: 0.5
                }
              ]
            },

            {
              fontSize: 8,
              columns: [
                {
                  text: "   DENTISMART ©",
                  margin: [30, 10, 0, 0],
                  width: 100,
                  alignment: "left",
                  color: [34, 133, 181],
                  bold: true
                },
                {
                  text: "Sistema de Gestión para consultorios dentales ",
                  width: 330,
                  margin: [10, 10, 0, 0],
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Version: 1.0.0",
                  color: [34, 133, 181],
                  width: 120,
                  margin: [5, 10, 10, 0],
                  alignment: "right",
                  bold: true
                }
              ]
            }
          ];
        },
        styles: {
          header: {
            fontSize: 18,
            bold: true
          },
          tableHeader: {
            border: 0
          },
          columnas: {
            fillColor: "skyblue"
          },
          subheader: {
            fontSize: 14,
            bold: true,
            margin: [0, 15, 0, 0]
          },
          story: {
            italic: true,
            alignment: "center",
            width: "50%"
          }
        }
      };
      pdfMake.createPdf(docDefinition).download("Dentismart.pdf");
      this.$vloading.hide();
    },

    pdfingresosgen: function() {
      var hoy = new Date();
      var reportetipo = this.tipoReporte;
      reportetipo = reportetipo.toUpperCase();
      var nombreconsultorio = this.consultorio.nombre;
      var fecha = this.hoyFecha();
      var base64 = this.getBase64Image(document.getElementById("img"));
      var grafica1 = this.getBase64Grafica(
        document.getElementById("MiGrafica")
      );

      var grafica2 = this.getBase64Grafica(
        document.getElementById("MiGrafica2")
      );

      var grafica3 = this.getBase64Grafica(
        document.getElementById("MiGrafica3")
      );

      /*             console.log(grafica2);
       */
      var hora =
        hoy.getHours() + ":" + hoy.getMinutes() + ":" + hoy.getSeconds();

      var ingresos = this.ingresosTotale;
      var fechainicio = this.fecha1 + "";

      var iniciodia = parseInt(fechainicio.substring(8));
      var iniciomes = parseInt(fechainicio.substring(5, 7));
      var inicioaño = parseInt(fechainicio.substring(0, 4));

      var fechafinal = this.fecha2 + "";
      var finaldia = parseInt(fechafinal.substring(8));
      var finalmes = parseInt(fechafinal.substring(5, 7));
      var finalaño = parseInt(fechafinal.substring(0, 4));

      fechainicio = iniciodia + "/" + iniciomes + "/" + inicioaño;
      fechafinal = finaldia + "/" + finalmes + "/" + finalaño;

      var tabla = [];

      var column = [];
      column.push({
        text: "Fecha y hora",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Servicio",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Dentista",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Paciente",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Total",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      tabla.push(column);

      console.log(this.citasxdentista);

      this.citasxdentista.forEach(element => {
        element.servicios.forEach(x => {
          column = [];

          var ms = Date.parse(element.fechaCita);
          var fecha2 = new Date(ms);

          var auxfechaaux = fecha2;
          fecha2 = this.obtenerFecha(fecha2);

          if (
            auxfechaaux >= this.iniciofecha &&
            auxfechaaux <= this.finalfecha
          ) {
            column.push({ text: fecha2, style: "tableHeader" });
            column.push({ text: x.nombre, style: "tableHeader" });
            column.push({
              text: element.dentista.nombreUsuario,
              style: "tableHeader"
            });
            column.push({
              text: element.paciente.nombreUsuario,
              style: "tableHeader"
            });
            column.push({ text: "$" + x.precio, style: "tableHeader" });
            tabla.push(column);
          }
        });
      });

      console.log(tabla);

      var pdfMake = require("pdfmake/build/pdfmake.js");
      if (pdfMake.vfs == undefined) {
        var pdfFonts = require("pdfmake/build/vfs_fonts.js");
        pdfMake.vfs = pdfFonts.pdfMake.vfs;
      }
      var docDefinition = {
        pageSize: "LETTER",
        pageMargins: [25, 130, 25, 35],

        header: function(currentPage, pageCount) {
          return [
            //margin : [ izquierda, arriba, derecha, abajo ]

            {
              image: base64,
              style: "subheader",
              margin: [10, 10, 0, 0],
              alignment: "center"
            },

            {
              fontSize: 10,
              columns: [
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "left",
                  bold: true
                },
                {
                  text: "REPORTE GRAFICO DE " + reportetipo,
                  width: 400,
                  margin: [10, 5, 0, 0],
                  alignment: "center",
                  bold: true,
                  fontSize: 15
                },
                {
                  text:
                    "Página: " + currentPage.toString() + " de " + pageCount,
                  margin: [10, 10, 20, 0],
                  alignment: "right"
                }
              ]
            },

            {
              fontSize: 10,
              columns: [
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "left",
                  bold: true
                },
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Fecha: " + fecha,
                  margin: [10, 5, 20, 0],
                  alignment: "right"
                }
              ]
            },
            {
              fontSize: 10,
              columns: [
                {
                  fontSize: 12,
                  text: "Consultorio: " + nombreconsultorio,
                  margin: [20, 5, 0, 0],
                  width: 170,
                  alignment: "left",
                  bold: true
                },
                {
                  fontSize: 12,
                  text: "Rango de fechas: " + fechainicio + " - " + fechafinal,
                  margin: [10, 5, 0, 0],
                  width: 300,
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Hora: " + hora,
                  margin: [10, 5, 20, 0],
                  alignment: "right"
                }
              ]
            }
          ];
        },
        content: [
          {},
          {
            text: "Ingresos Registrados",
            width: 400,
            margin: [10, 5, 0, 10],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            table: {
              // Defining the top 2 rows as the "sticky" header rows
              headerRows: 1,
              // One column, full width
              widths: ["20%", "20%", "20%", "20%", "20%"],
              body: tabla,

              layout: "noBorders"
            }
          },

          {
            fontSize: 12,
            text: "Total: $" + ingresos,
            height: 10,
            width: 100,
            margin: [470, 10, 0, 0]
          },

          {
            text: "Ingresos por mes",
            width: 400,
            margin: [10, 5, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            image: grafica1,
            style: "subheader",
            margin: [10, 10, 0, 0],
            alignment: "center"
          },
          {
            text: "Ingresos por año",
            width: 400,
            margin: [10, 5, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            image: grafica2,
            style: "subheader",
            margin: [10, 10, 0, 0],
            alignment: "center"
          },
          {
            text: "Servicios con más ingresos producidos",
            width: 400,
            margin: [10, 5, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            image: grafica3,
            style: "subheader",
            margin: [10, 10, 0, 0],
            alignment: "center"
          }
        ],

        footer: function() {
          return [
            {
              canvas: [
                {
                  type: "line",
                  color: [0, 0, 0],
                  x1: 10,
                  y1: 10,
                  x2: 590,
                  y2: 10,
                  lineWidth: 0.5
                }
              ]
            },

            {
              fontSize: 8,
              columns: [
                {
                  text: "   DENTISMART ©",
                  margin: [30, 10, 0, 0],
                  width: 100,
                  alignment: "left",
                  color: [34, 133, 181],
                  bold: true
                },
                {
                  text: "Sistema de Gestión para consultorios dentistas ",
                  width: 330,
                  margin: [10, 10, 0, 0],
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Version: 1.0.0",
                  color: [34, 133, 181],
                  width: 120,
                  margin: [5, 10, 10, 0],
                  alignment: "right",
                  bold: true
                }
              ]
            }
          ];
        },
        styles: {
          header: {
            fontSize: 18,
            bold: true
          },
          tableHeader: {
            border: 0
          },
          columnas: {
            fillColor: "skyblue"
          },
          subheader: {
            fontSize: 14,
            bold: true,
            margin: [0, 15, 0, 0]
          },
          story: {
            italic: true,
            alignment: "center",
            width: "50%"
          }
        }
      };
      pdfMake.createPdf(docDefinition).download("Dentismart.pdf");
      this.$vloading.hide();
    },
    pdfhistorialgen: function() {
      var hoy = new Date();

      var nombreconsultorio = this.pacienteModel.nombreUsuario;
      var fecha = this.hoyFecha();
      var base64 = this.getBase64Image(document.getElementById("img"));

      var hora =
        hoy.getHours() + ":" + hoy.getMinutes() + ":" + hoy.getSeconds();

      var fechainicio = this.fecha3 + "";

      var iniciodia = parseInt(fechainicio.substring(8));
      var iniciomes = parseInt(fechainicio.substring(5, 7));
      var inicioaño = parseInt(fechainicio.substring(0, 4));

      var fechafinal = this.fecha4 + "";
      var finaldia = parseInt(fechafinal.substring(8));
      var finalmes = parseInt(fechafinal.substring(5, 7));
      var finalaño = parseInt(fechafinal.substring(0, 4));

      fechainicio = iniciodia + "/" + iniciomes + "/" + inicioaño;
      fechafinal = finaldia + "/" + finalmes + "/" + finalaño;

      var tabla = [];

      var column = [];
      column.push({
        text: "Fecha y hora",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Dentista",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Servicio",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Descripción",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column.push({
        text: "Precio",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      tabla.push(column);

      var banderaTratamiento = false;

      this.historialPaciente.forEach(element => {
        element.servicios.forEach(x => {
          column = [];

          var ms = Date.parse(element.fechaCita);
          var fecha2 = new Date(ms);

          var auxfechaaux = fecha2;
          fecha2 = this.obtenerFecha(fecha2);

          if (
            auxfechaaux >= this.iniciofecha &&
            auxfechaaux <= this.finalfecha
          ) {
            banderaTratamiento = true;
            column.push({ text: fecha2, style: "tableHeader" });
            column.push({
              text: element.dentista.nombreUsuario,
              style: "tableHeader"
            });
            column.push({
              text: x.nombre,
              style: "tableHeader"
            });
            column.push({
              text: x.descripcion,
              style: "tableHeader"
            });
            column.push({ text: "$" + x.precio, style: "tableHeader" });
            tabla.push(column);
          }
        });
      });

      if (!banderaTratamiento == true) {
        this.$toast.error(
          "No se encontrados datos relacionados dentro de las fechas proporcionadas"
        );
        this.$vloading.hide();
        return;
      }

      var tratamiento = [];
      var odontogramasuperior = [];
      var odontogramainferior = [];

      var column2 = [];
      column2.push({
        text: "Fecha y hora",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column2.push({
        text: "Dentista",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column2.push({
        text: "Sintomas",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column2.push({
        text: "Observación",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column2.push({
        text: "Receta",
        width: 200,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });

      tratamiento.push(column2);

      var column3 = [];
      column3.push({
        text: "Fecha y hora",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column3.push({
        text: "No. Diente",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column3.push({
        text: "Izquierdo",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column3.push({
        text: "Norte",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column3.push({
        text: "Centro",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column3.push({
        text: "Derecho",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column3.push({
        text: "Sur",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });

      var column4 = [];
      column4.push({
        text: "Fecha y hora",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column4.push({
        text: "No. Diente",
        width: 200,
        border: [true, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column4.push({
        text: "Izquierdo",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column4.push({
        text: "Norte",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column4.push({
        text: "Centro",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column4.push({
        text: "Derecho",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      column4.push({
        text: "Sur",
        width: 100,
        border: [false, true, true, true],
        style: "tableHeader",
        fillColor: "skyblue"
      });
      odontogramainferior.push(column4);
      odontogramasuperior.push(column3);

      console.log(this.tratamientoPaciente);
      this.tratamientoPaciente.forEach(element => {
        var ms = Date.parse(element.cita.fechaCita);
        var fecha2 = new Date(ms);

        var auxfechaaux = fecha2;
        fecha2 = this.obtenerFecha(fecha2);

        console.log(element.odontograma);

        if (auxfechaaux >= this.iniciofecha && auxfechaaux <= this.finalfecha) {
          var cont = 1;
          element.odontograma.dientesSuperior.forEach(superior => {
            column4 = [];

            column4.push({ text: fecha2, style: "tableHeader" });
            column4.push({ text: "Diente #" + cont, style: "tableHeader" });
            column4.push({
              text: this.obtenerValorOdontograma(superior.izquierdo),
              style: "tableHeader"
            });
            column4.push({
              text: this.obtenerValorOdontograma(superior.norte),
              style: "tableHeader"
            });
            column4.push({
              text: this.obtenerValorOdontograma(superior.centro),
              style: "tableHeader"
            });
            column4.push({
              text: this.obtenerValorOdontograma(superior.derecho),
              style: "tableHeader"
            });
            column4.push({
              text: this.obtenerValorOdontograma(superior.sur),
              style: "tableHeader"
            });

            cont++;
            odontogramasuperior.push(column4);
          });

          cont = 1;
          element.odontograma.dientesInferior.forEach(inferior => {
            column3 = [];

            column3.push({ text: fecha2, style: "tableHeader" });
            column3.push({ text: "Diente #" + cont, style: "tableHeader" });
            column3.push({
              text: this.obtenerValorOdontograma(inferior.izquierdo),
              style: "tableHeader"
            });
            column3.push({
              text: this.obtenerValorOdontograma(inferior.norte),
              style: "tableHeader"
            });
            column3.push({
              text: this.obtenerValorOdontograma(inferior.centro),
              style: "tableHeader"
            });
            column3.push({
              text: this.obtenerValorOdontograma(inferior.derecho),
              style: "tableHeader"
            });
            column3.push({
              text: this.obtenerValorOdontograma(inferior.sur),
              style: "tableHeader"
            });

            cont++;
            odontogramainferior.push(column3);
          });
        }

        element.detalle.forEach(x => {
          column2 = [];

          if (
            auxfechaaux >= this.iniciofecha &&
            auxfechaaux <= this.finalfecha
          ) {
            column2.push({ text: fecha2, style: "tableHeader" });
            column2.push({
              text: element.cita.dentista.nombreUsuario,
              style: "tableHeader"
            });
            column2.push({ text: x.sintomas, style: "tableHeader" });
            column2.push({ text: x.observacion, style: "tableHeader" });
            column2.push({
              text: x.receta,
              style: "tableHeader"
            });

            tratamiento.push(column2);
          }
        });
      });

      var pdfMake = require("pdfmake/build/pdfmake.js");
      if (pdfMake.vfs == undefined) {
        var pdfFonts = require("pdfmake/build/vfs_fonts.js");
        pdfMake.vfs = pdfFonts.pdfMake.vfs;
      }
      var docDefinition = {
        pageSize: "LETTER",
        pageMargins: [25, 130, 25, 35],

        header: function(currentPage, pageCount) {
          return [
            //margin : [ izquierda, arriba, derecha, abajo ]

            {
              image: base64,
              style: "subheader",
              margin: [10, 10, 0, 0],
              alignment: "center"
            },

            {
              fontSize: 10,
              columns: [
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "left",
                  bold: true
                },
                {
                  text: "Historial Clinico",
                  width: 400,
                  margin: [10, 5, 0, 0],
                  alignment: "center",
                  bold: true,
                  fontSize: 15
                },
                {
                  text:
                    "Página: " + currentPage.toString() + " de " + pageCount,
                  margin: [10, 10, 20, 0],
                  alignment: "right"
                }
              ]
            },

            {
              fontSize: 10,
              columns: [
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "left",
                  bold: true
                },
                {
                  text: "",
                  margin: [10, 5, 0, 0],
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Fecha: " + fecha,
                  margin: [10, 5, 20, 0],
                  alignment: "right"
                }
              ]
            },
            {
              fontSize: 10,
              columns: [
                {
                  fontSize: 12,
                  text: "Paciente: " + nombreconsultorio,
                  margin: [20, 5, 0, 0],
                  width: 170,
                  alignment: "left",
                  bold: true
                },
                {
                  fontSize: 12,
                  text: "Rango de fechas: " + fechainicio + " - " + fechafinal,
                  margin: [10, 5, 0, 0],
                  width: 300,
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Hora: " + hora,
                  margin: [10, 5, 20, 0],
                  alignment: "right"
                }
              ]
            }
          ];
        },
        content: [
          {},
          {
            text: "Servicios registrados",
            width: 400,
            margin: [10, 5, 0, 10],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            table: {
              // Defining the top 2 rows as the "sticky" header rows
              headerRows: 1,
              // One column, full width
              widths: ["20%", "20%", "20%", "20%", "20%"],
              body: tabla,

              layout: "noBorders"
            }
          },

          {
            text: "  ",
            width: 400,
            margin: [0, 30, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            text: "Tratamientos registrados",
            width: 400,
            margin: [10, 5, 0, 10],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            table: {
              // Defining the top 2 rows as the "sticky" header rows
              headerRows: 1,
              // One column, full width
              widths: ["20%", "20%", "20%", "20%", "20%"],
              body: tratamiento,

              layout: "noBorders"
            }
          },

          {
            text: "  ",
            width: 400,
            margin: [0, 30, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            text: "Dientes Superiores",
            width: 400,
            margin: [10, 5, 0, 10],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            table: {
              // Defining the top 2 rows as the "sticky" header rows
              headerRows: 1,
              widths: [
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%"
              ],
              // One column, full width
              body: odontogramasuperior,

              layout: "noBorders"
            }
          },
          {
            text: "  ",
            width: 400,
            margin: [0, 30, 0, 0],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            text: "Dientes Inferiores",
            width: 400,
            margin: [10, 5, 0, 10],
            alignment: "center",
            bold: true,
            fontSize: 15
          },
          {
            table: {
              // Defining the top 2 rows as the "sticky" header rows
              headerRows: 1,
              widths: [
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%",
                "14.2%"
              ],
              // One column, full width
              body: odontogramainferior,

              layout: "noBorders"
            }
          }
        ],

        footer: function() {
          return [
            {
              canvas: [
                {
                  type: "line",
                  color: [0, 0, 0],
                  x1: 10,
                  y1: 10,
                  x2: 590,
                  y2: 10,
                  lineWidth: 0.5
                }
              ]
            },

            {
              fontSize: 8,
              columns: [
                {
                  text: "   DENTISMART ©",
                  margin: [30, 10, 0, 0],
                  width: 100,
                  alignment: "left",
                  color: [34, 133, 181],
                  bold: true
                },
                {
                  text: "Sistema de Gestión para consultorios dentistas ",
                  width: 330,
                  margin: [10, 10, 0, 0],
                  alignment: "center",
                  bold: true
                },
                {
                  text: "Version: 1.0.0",
                  color: [34, 133, 181],
                  width: 120,
                  margin: [5, 10, 10, 0],
                  alignment: "right",
                  bold: true
                }
              ]
            }
          ];
        },
        styles: {
          header: {
            fontSize: 18,
            bold: true
          },
          tableHeader: {
            border: 0
          },
          columnas: {
            fillColor: "skyblue"
          },
          subheader: {
            fontSize: 14,
            bold: true,
            margin: [0, 15, 0, 0]
          },
          story: {
            italic: true,
            alignment: "center",
            width: "50%"
          }
        }
      };
      pdfMake.createPdf(docDefinition).download("Dentismart.pdf");
      this.$vloading.hide();
    },
    metodoPDF() {},
    ObtenerDatos() {
      if (this.tipoReporte == "Gastos") {
        if (
          this.consultorio == null ||
          this.fecha1 == null ||
          this.fecha2 == null
        ) {
          this.$toast.error("Error: Datos incompletos");
          return;
        }
      } else if (this.tipoReporte == "Ingresos") {
        if (
          this.consultorio == null ||
          this.dentista == null ||
          this.fecha1 == null ||
          this.fecha2 == null
        ) {
          this.$toast.error("Error: Datos incompletos");
          return;
        }
      } else if (this.auxTipo == "Historial Clinico") {
        if (
          this.pacienteModel == null ||
          this.fecha3 == null ||
          this.fecha4 == null
        ) {
          this.$toast.error("Error: Datos incompletos");
          return;
        }
      }

      var fechahoy = null;
      if (this.tipoReporte == "Ingresos" || this.tipoReporte == "Gastos") {
        var ban = Date.parse(this.fecha1);
        this.iniciofecha = new Date(ban);
        var ban2 = Date.parse(this.fecha2);
        this.finalfecha = new Date(ban2);
        var ban3 = Date.parse(new Date());
        fechahoy = new Date(ban3);
      } else {
        var ban4 = Date.parse(this.fecha3);
        this.iniciofecha = new Date(ban4);
        var ban5 = Date.parse(this.fecha4);
        this.finalfecha = new Date(ban5);
        var ban6 = Date.parse(new Date());
        fechahoy = new Date(ban6);
      }

      if (this.iniciofecha > this.finalfecha) {
        this.$toast.error(
          "Error: La fecha final no puede ser mayor que la fecha inicial"
        );
        return;
      }

      if (this.iniciofecha > fechahoy) {
        this.$toast.error(
          "Error: La fecha inicial no debe ser mayor a la fecha actual"
        );
        return;
      }

      this.gastosxYear = [];
      this.gastosxMes.forEach(gxM => {
        gxM.gastos = 0;
      });

      this.$vloading.show();

      if (this.tipoReporte == "Gastos") {
        this.productosMasGastos = [];

        axios
          .get(
            environment.url + "Inventario/Consultorio/" + this.consultorio.id
          )
          .then(response => {
            this.gastosTotales = 0;
            this.auxProductosGastos = response.data;
            response.data.forEach(gasto => {
              var ms = Date.parse(gasto.fechaModificacion);
              var fecha = new Date(ms);
              /*             var auxDia = fecha.getDay();
               */ var auxMes = fecha.getMonth() + 1;
              var auxYear = fecha.getFullYear();

              var ms2 = Date.parse(this.fecha1);
              this.iniciofecha = new Date(ms2);
              this.iniciofecha.setMinutes(
                this.iniciofecha.getMinutes() +
                  this.iniciofecha.getTimezoneOffset()
              );
              this.iniciofecha.setHours(23, 59, 59, 999);
              var ms3 = Date.parse(this.fecha2);
              this.finalfecha = new Date(ms3);
              this.finalfecha.setMinutes(
                this.finalfecha.getMinutes() +
                  this.finalfecha.getTimezoneOffset()
              );
              this.finalfecha.setHours(23, 59, 59, 999);

              if (fecha >= this.iniciofecha && fecha <= this.finalfecha) {
                gasto.productosInventario.forEach(prod => {
                  if (prod.cantidadActualizada > prod.cantidadAnterior) {
                    var auxproducto = {
                      producto: prod.nombre,
                      gasto:
                        prod.precio *
                        (prod.cantidadActualizada - prod.cantidadAnterior)
                    };

                    if (this.productosMasGastos.length == 0)
                      this.productosMasGastos.push(auxproducto);
                    else {
                      var bandera = false;
                      this.productosMasGastos.forEach(uwu => {
                        if (uwu.producto == auxproducto.producto) {
                          uwu.gasto =
                            uwu.gasto +
                            prod.precio *
                              (prod.cantidadActualizada -
                                prod.cantidadAnterior);
                          bandera = true;
                          return;
                        }
                      });

                      if (!bandera) {
                        this.productosMasGastos.push(auxproducto);
                      }
                    }

                    this.gastosTotales +=
                      prod.precio *
                      (prod.cantidadActualizada - prod.cantidadAnterior);

                    this.gastosxMes.forEach(mes => {
                      if (mes.Mes == auxMes)
                        mes.gastos +=
                          prod.precio *
                          (prod.cantidadActualizada - prod.cantidadAnterior);
                    });

                    if (this.gastosxYear.length == 0) {
                      var auxAnio = {
                        año: auxYear,
                        total:
                          prod.precio *
                          (prod.cantidadActualizada - prod.cantidadAnterior)
                      };
                      this.gastosxYear.push(auxAnio);
                    } else {
                      this.gastosxYear.forEach(year => {
                        if (year.año == auxYear)
                          year.total +=
                            prod.precio *
                            (prod.cantidadActualizada - prod.cantidadAnterior);
                      });
                    }
                  }
                });
              }
            });

            if (this.gastosTotales == 0) {
              this.$toast.error(
                "No se encontrados datos relacionados dentro de las fechas proporcionadas"
              );
              this.$vloading.hide();
              return;
            }
            var contador = 1;
            var min = this.gastosxYear[0].año;
            var max = this.gastosxYear[0].año;

            this.gastosxYear.forEach(uwu => {
              if (uwu.año < min) min = uwu.año;

              if (uwu.año > max) max = uwu.año;
            });

            for (let i = 0; i < 2; i++) {
              var auxAnio2 = {
                año: min - contador,
                total: 0
              };

              var auxAnio3 = {
                año: max + contador,
                total: 0
              };

              this.gastosxYear.unshift(auxAnio2);
              this.gastosxYear.push(auxAnio3);
              contador++;
            }

            let miCanvas = document
              .getElementById("MiGrafica")
              .getContext("2d");

            miCanvas.width = 500;
            miCanvas.height = 280;

            var mango = [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ];
            var jicama = [];

            this.gastosxMes.forEach(man => {
              jicama.push(man.gastos);
            });

            var chart = new Chart(miCanvas, {
              type: "line",
              data: {
                labels: mango,
                datasets: [
                  {
                    label: "Gastos por Mes",
                    backgroundColor: [
                      "rgba(255, 99, 132, 0.2)",
                      "rgba(54, 162, 235, 0.2)",
                      "rgba(255, 206, 86, 0.2)",
                      "rgba(75, 192, 192, 0.2)",
                      "rgba(153, 102, 255, 0.2)",
                      "rgba(255, 159, 64, 0.2)"
                    ],
                    borderColor: [
                      "rgba(255,99,132,1)",
                      "rgba(54, 162, 235, 1)",
                      "rgba(255, 206, 86, 1)",
                      "rgba(75, 192, 192, 1)",
                      "rgba(153, 102, 255, 1)",
                      "rgba(255, 159, 64, 1)"
                    ],
                    borderWidth: 1,
                    data: jicama
                  }
                ]
              },
              options: {
                scales: {
                  yAxes: [
                    {
                      ticks: {
                        suggestedMin: 50,
                        suggestedMax: 100
                      }
                    }
                  ]
                },
                responsive: false,
                animation: false
              }
            });
            console.log(chart);

            let miCanvas2 = document
              .getElementById("MiGrafica2")
              .getContext("2d");

            var manzana = [];
            var pera = [];

            this.gastosxYear.forEach(man => {
              manzana.push(man.año);
              pera.push(man.total);
            });

            var chart2 = new Chart(miCanvas2, {
              type: "line",
              data: {
                labels: manzana,
                datasets: [
                  {
                    label: "Gastos por Año",
                    backgroundColor: [
                      "rgba(255, 99, 132, 0.2)",
                      "rgba(54, 162, 235, 0.2)",
                      "rgba(255, 206, 86, 0.2)",
                      "rgba(75, 192, 192, 0.2)",
                      "rgba(153, 102, 255, 0.2)",
                      "rgba(255, 159, 64, 0.2)"
                    ],
                    borderColor: [
                      "rgba(255,99,132,1)",
                      "rgba(54, 162, 235, 1)",
                      "rgba(255, 206, 86, 1)",
                      "rgba(75, 192, 192, 1)",
                      "rgba(153, 102, 255, 1)",
                      "rgba(255, 159, 64, 1)"
                    ],
                    borderWidth: 1,
                    data: pera
                  }
                ]
              },
              options: {
                scales: {
                  yAxes: [
                    {
                      ticks: {
                        suggestedMin: 50,
                        suggestedMax: 100
                      }
                    }
                  ]
                },
                responsive: false,
                animation: false
              }
            });
            console.log(chart2);

            this.productosMasGastos.sort((a, b) => b.gasto - a.gasto);

            let miCanvas3 = document
              .getElementById("MiGrafica3")
              .getContext("2d");

            var productos = [];
            var gastos = [];
            this.productosMasGastos.forEach(man => {
              productos.push(man.producto);
              gastos.push(man.gasto);
            });

            var chart3 = new Chart(miCanvas3, {
              type: "bar",
              data: {
                labels: productos,
                datasets: [
                  {
                    label: "Productos con mayor gasto",

                    backgroundColor: [
                      "rgba(255, 99, 132, 0.2)",
                      "rgba(54, 162, 235, 0.2)",
                      "rgba(255, 206, 86, 0.2)",
                      "rgba(75, 192, 192, 0.2)",
                      "rgba(153, 102, 255, 0.2)",
                      "rgba(255, 159, 64, 0.2)"
                    ],
                    borderColor: [
                      "rgba(255,99,132,1)",
                      "rgba(54, 162, 235, 1)",
                      "rgba(255, 206, 86, 1)",
                      "rgba(75, 192, 192, 1)",
                      "rgba(153, 102, 255, 1)",
                      "rgba(255, 159, 64, 1)"
                    ],
                    borderWidth: 1,
                    data: gastos
                  }
                ]
              },
              options: {
                scales: {
                  yAxes: [
                    {
                      ticks: {
                        suggestedMin: 50,
                        suggestedMax: 100
                      }
                    }
                  ]
                },
                responsive: false,
                animation: false
              }
            });
            console.log(chart3);
            this.pdfgastosgen();
            this.vergraficas = true;
            this.gastosaux = true;
          })
          .catch(() => {
            this.$vloading.hide();
          });
      } else if (this.tipoReporte == "Ingresos") {
        console.log(this.consultorio);
        console.log(this.dentista);
        this.serviciosMasIngresos = [];
        this.$vloading.show();

        axios
          .get(environment.url + "Cita/Dentista/" + this.dentista.id)
          .then(response => {
            console.log(response.data);

            this.ingresosTotale = 0;
            this.citasxdentista = response.data;
            console.log(this.citasxdentista);
            this.citasxdentista.forEach(cita => {
              var ms = Date.parse(cita.fechaCita);
              var fecha = new Date(ms);
              var auxMes = fecha.getMonth() + 1;
              var auxYear = fecha.getFullYear();

              var ms2 = Date.parse(this.fecha1);
              this.iniciofecha = new Date(ms2);
              this.iniciofecha.setMinutes(
                this.iniciofecha.getMinutes() +
                  this.iniciofecha.getTimezoneOffset()
              );
              this.iniciofecha.setHours(23, 59, 59, 999);
              var ms3 = Date.parse(this.fecha2);
              this.finalfecha = new Date(ms3);
              this.finalfecha.setMinutes(
                this.finalfecha.getMinutes() +
                  this.finalfecha.getTimezoneOffset()
              );
              this.finalfecha.setHours(23, 59, 59, 999);

              if (fecha >= this.iniciofecha && fecha <= this.finalfecha) {
                cita.servicios.forEach(uwu => {
                  var auxServicio = {
                    producto: uwu.nombre,
                    gasto: uwu.precio
                  };
                  this.ingresosTotale += auxServicio.gasto;

                  if (this.serviciosMasIngresos.length == 0)
                    this.serviciosMasIngresos.push(auxServicio);
                  else {
                    var bandera = false;
                    this.serviciosMasIngresos.forEach(uwu2 => {
                      if (uwu2.producto == auxServicio.producto) {
                        uwu2.gasto += auxServicio.gasto;
                        bandera = true;
                        return;
                      }
                    });

                    if (!bandera) {
                      this.serviciosMasIngresos.push(auxServicio);
                    }
                  }

                  this.gastosxMes.forEach(mes => {
                    if (mes.Mes == auxMes) mes.gastos += uwu.precio;
                  });

                  if (this.gastosxYear.length == 0) {
                    var auxAnio = {
                      año: auxYear,
                      total: uwu.precio
                    };
                    this.gastosxYear.push(auxAnio);
                  } else {
                    this.gastosxYear.forEach(year => {
                      if (year.año == auxYear) year.total += uwu.precio;
                    });
                  }
                });
              }
            });

            if (this.ingresosTotale == 0) {
              this.$toast.error(
                "No se encontrados datos relacionados dentro de las fechas proporcionadas"
              );
              this.$vloading.hide();
              return;
            }
            var contador = 1;
            var min = this.gastosxYear[0].año;
            var max = this.gastosxYear[0].año;

            this.gastosxYear.forEach(uwu => {
              if (uwu.año < min) min = uwu.año;

              if (uwu.año > max) max = uwu.año;
            });

            for (let i = 0; i < 2; i++) {
              var auxAnio2 = {
                año: min - contador,
                total: 0
              };

              var auxAnio3 = {
                año: max + contador,
                total: 0
              };

              this.gastosxYear.unshift(auxAnio2);
              this.gastosxYear.push(auxAnio3);
              contador++;
            }
            console.log(this.gastosxMes);
            console.log(this.gastosxYear);
            console.log(this.serviciosMasIngresos);

            let miCanvas4 = document
              .getElementById("MiGrafica")
              .getContext("2d");

            var mango = [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ];
            var jicama = [];

            this.gastosxMes.forEach(man => {
              jicama.push(man.gastos);
            });

            var chart4 = new Chart(miCanvas4, {
              type: "line",
              data: {
                labels: mango,
                datasets: [
                  {
                    label: "Ingresos por Mes",
                    backgroundColor: [
                      "rgba(255, 99, 132, 0.2)",
                      "rgba(54, 162, 235, 0.2)",
                      "rgba(255, 206, 86, 0.2)",
                      "rgba(75, 192, 192, 0.2)",
                      "rgba(153, 102, 255, 0.2)",
                      "rgba(255, 159, 64, 0.2)"
                    ],
                    borderColor: [
                      "rgba(255,99,132,1)",
                      "rgba(54, 162, 235, 1)",
                      "rgba(255, 206, 86, 1)",
                      "rgba(75, 192, 192, 1)",
                      "rgba(153, 102, 255, 1)",
                      "rgba(255, 159, 64, 1)"
                    ],
                    borderWidth: 1,
                    data: jicama
                  }
                ]
              },
              options: {
                scales: {
                  yAxes: [
                    {
                      ticks: {
                        suggestedMin: 50,
                        suggestedMax: 100
                      }
                    }
                  ]
                },
                responsive: false,
                animation: false
              }
            });
            console.log(chart4);

            let miCanvas5 = document
              .getElementById("MiGrafica2")
              .getContext("2d");

            var manzana = [];
            var pera = [];

            this.gastosxYear.forEach(man => {
              manzana.push(man.año);
              pera.push(man.total);
            });

            var chart5 = new Chart(miCanvas5, {
              type: "line",
              data: {
                labels: manzana,
                datasets: [
                  {
                    label: "Ingresos por Año",
                    backgroundColor: [
                      "rgba(255, 99, 132, 0.2)",
                      "rgba(54, 162, 235, 0.2)",
                      "rgba(255, 206, 86, 0.2)",
                      "rgba(75, 192, 192, 0.2)",
                      "rgba(153, 102, 255, 0.2)",
                      "rgba(255, 159, 64, 0.2)"
                    ],
                    borderColor: [
                      "rgba(255,99,132,1)",
                      "rgba(54, 162, 235, 1)",
                      "rgba(255, 206, 86, 1)",
                      "rgba(75, 192, 192, 1)",
                      "rgba(153, 102, 255, 1)",
                      "rgba(255, 159, 64, 1)"
                    ],
                    borderWidth: 1,
                    data: pera
                  }
                ]
              },
              options: {
                scales: {
                  yAxes: [
                    {
                      ticks: {
                        suggestedMin: 50,
                        suggestedMax: 100
                      }
                    }
                  ]
                },
                responsive: false,
                animation: false
              }
            });
            console.log(chart5);

            console.log(this.serviciosMasIngresos);

            let miCanvas6 = document
              .getElementById("MiGrafica3")
              .getContext("2d");

            var productos = [];
            var gastos = [];

            this.serviciosMasIngresos.forEach(man => {
              productos.push(man.producto);
              gastos.push(man.gasto);
            });

            console.log(this.serviciosMasIngresos);
            console.log(productos);
            console.log(gastos);

            var chart6 = new Chart(miCanvas6, {
              type: "bar",
              data: {
                labels: productos,
                datasets: [
                  {
                    label: "Servicios con mayor ingreso",

                    backgroundColor: [
                      "rgba(255, 99, 132, 0.2)",
                      "rgba(240, 162, 235, 0.2)",
                      "rgba(255, 206, 86, 0.2)",
                      "rgba(75, 192, 192, 0.2)",
                      "rgba(153, 102, 255, 0.2)",
                      "rgba(255, 159, 64, 0.2)"
                    ],
                    borderColor: [
                      "rgba(255,99,132,1)",
                      "rgba(54, 162, 235, 1)",
                      "rgba(255, 206, 86, 1)",
                      "rgba(75, 192, 192, 1)",
                      "rgba(153, 102, 255, 1)",
                      "rgba(255, 159, 64, 1)"
                    ],
                    borderWidth: 1,
                    data: gastos
                  }
                ]
              },

              options: {
                scales: {
                  yAxes: [
                    {
                      ticks: {
                        suggestedMin: 50,
                        suggestedMax: 100
                      }
                    }
                  ]
                },
                responsive: false,
                animation: false
              }
            });
            console.log(chart6);
            this.ingresosaux = true;
            this.vergraficas = true;
            this.pdfingresosgen();
          })
          .catch(() => {
            this.$vloading.hide();
            this.$toast.error("No se pudieron obtener los datos");
          });
      } else if (this.auxTipo == "Historial Clinico") {
        console.log(this.pacienteModel);
        axios
          .get(environment.url + "Cita/Paciente/" + this.pacienteModel.id)
          .then(response => {
            this.historialPaciente = response.data;

            axios
              .get(
                environment.url +
                  "Tratamiento/Paciente/" +
                  this.pacienteModel.id
              )
              .then(response2 => {
                this.tratamientoPaciente = response2.data;
                console.log(this.tratamientoPaciente);
                this.pdfhistorialgen();
              })
              .catch(() => {
                this.$vloading.hide();
              });
          })
          .catch(() => {
            this.$vloading.hide();
          });
      }
    },

    obtenerValorOdontograma(diente) {
      if (diente == 1) return "Carie";
      else if (diente == 2) return "Amalgama";
      else if (diente == 3) return "Endodoncia";
      else if (diente == 4) return "Ausente";
      else if (diente == 5) return "Resina";
      else if (diente == 6) return "Implante";
      else if (diente == 7) return "Sellante";
      else if (diente == 8) return "Corona";
      else return "Normal";
    }
  }
};
</script>

<style lang="scss" scoped>
.compact-form {
  transform: scale(0.875);
  transform-origin: center;
}

.container {
  padding: 0px !important;
}

.subir-formulario {
  position: relative;
  top: -30px;
}
</style>
