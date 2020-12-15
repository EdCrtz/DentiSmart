<template>
  <v-card flat>
    <div
      v-if="dialogPago || dialogDelete || dialogTratamiento"
      class="v-overlay v-overlay--active theme--dark"
      style="z-index: 207;"
    >
      <div
        class="v-overlay__scrim"
        style="opacity: 0.46; background-color: rgb(33, 33, 33); border-color: rgb(33, 33, 33);"
      ></div>
      <div class="v-overlay__content"></div>
    </div>
    <v-card-text>
      <v-row class="fill-height">
        <v-col>
          <v-sheet height="64">
            <v-toolbar flat>
              <v-btn outlined class="mr-4" color="primary" @click="setToday">
                Hoy
              </v-btn>
              <v-btn fab text small color="primary" @click="prev">
                <v-icon small>
                  mdi-chevron-left
                </v-icon>
              </v-btn>
              <v-btn fab text small color="primary" @click="next">
                <v-icon small>
                  mdi-chevron-right
                </v-icon>
              </v-btn>
              <v-toolbar-title v-if="$refs.calendar">
                {{ $refs.calendar.title }}
              </v-toolbar-title>
              <v-spacer></v-spacer>
              <v-menu bottom right>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn outlined color="primary" v-bind="attrs" v-on="on">
                    <span>{{ typeToLabel[type] }}</span>
                    <v-icon right>
                      mdi-menu-down
                    </v-icon>
                  </v-btn>
                </template>
                <v-list>
                  <v-list-item @click="type = 'day'">
                    <v-list-item-title>Dia</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="type = 'week'">
                    <v-list-item-title>Semana</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="type = 'month'">
                    <v-list-item-title>Mes</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="type = '4day'">
                    <v-list-item-title>4 Dias</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-toolbar>
          </v-sheet>
          <v-sheet height="400">
            <v-calendar
              ref="calendar"
              v-model="focus"
              color="primary"
              :events="events"
              :event-color="
                item => (item.status == 'Pendiente' ? 'green' : 'red')
              "
              :event-name="getNombre"
              :type="type"
              @click:event="showEvent"
              @click:more="viewDay"
              @click:date="viewDay"
              @change="editable = false"
              :short-weekdays="false"
            ></v-calendar>
            <v-menu
              v-model="selectedOpen"
              :close-on-click="false"
              :close-on-content-click="false"
              :activator="selectedElement"
              offset-x
            >
              <v-card class="my-0" max-width="650" flat>
                <v-toolbar
                  :color="
                    citaSeleccionada.status == 'Pendiente' ? 'green' : 'red'
                  "
                  dark
                  flat
                >
                  <v-avatar :color="!paciente.foto ? 'orange' : ''" size="45">
                    <v-img v-if="paciente.foto" :src="paciente.foto" />
                  </v-avatar>
                  <v-spacer></v-spacer>
                  <v-toolbar-title
                    >Cita dental del paciente:
                    {{ nombrepaciente }}</v-toolbar-title
                  >
                  <v-spacer></v-spacer>
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        @click="editable = true"
                        v-bind="attrs"
                        v-on="on"
                      >
                        <v-icon>mdi-calendar-edit</v-icon>
                      </v-btn>
                    </template>
                    <span>Editar cita</span>
                  </v-tooltip>
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        @click="dialogDelete = true"
                        v-bind="attrs"
                        v-on="on"
                      >
                        <v-icon>mdi-calendar-remove</v-icon>
                      </v-btn>
                    </template>
                    <span>Cancelar cita</span>
                  </v-tooltip>
                </v-toolbar>
                <v-card-text>
                  <v-form ref="form">
                    <v-row>
                      <v-col cols="12">
                        <v-text-field
                          v-model="nombrepaciente"
                          outlined
                          dense
                          v-if="user.role != 'Paciente'"
                          :rules="rules.paciente"
                          required
                          disabled
                          label="Paciente"
                        >
                        </v-text-field>
                      </v-col>
                      <v-col cols="12" v-if="user.role != 'Dentista'">
                        <v-select
                          v-model="consultorio"
                          :items="consultorios"
                          item-value="id"
                          item-text="nombre"
                          label="Consultorio"
                          placeholder="Seleccione un consultorio"
                          outlined
                          dense
                          :menu-props="{ bottom: true, offsetY: true }"
                          @change="cargarDatosConsultorio"
                          :rules="rules.consultorio"
                          required
                          :disabled="!editable"
                        ></v-select>
                      </v-col>
                      <v-col cols="12">
                        <v-select
                          v-model="servicioscita"
                          :items="servicios"
                          :item-value="item => item"
                          item-text="nombre"
                          label="Servicio"
                          placeholder="Seleccione un servicio"
                          outlined
                          dense
                          :menu-props="{ bottom: true, offsetY: true }"
                          multiple
                          :rules="rules.servicioscita"
                          :disabled="!editable"
                          required
                          chips
                          deletable-chips
                        ></v-select>
                      </v-col>
                      <v-col cols="6" v-if="user.role != 'Dentista'">
                        <v-select
                          v-model="dentista"
                          :items="dentistas"
                          label="Dentista"
                          :item-text="
                            item =>
                              item.nombre +
                              ' ' +
                              item.apellidoPat +
                              ' ' +
                              item.apellidoMat
                          "
                          :item-value="item => item"
                          placeholder=" Seleccione un dentista"
                          outlined
                          dense
                          :menu-props="{ bottom: true, offsetY: true }"
                          :rules="rules.dentista"
                          required
                          :disabled="!editable"
                        >
                        </v-select>
                      </v-col>
                      <v-col cols="6">
                        <v-datetime-picker
                          label="Horario de cita"
                          v-model="fechaCita"
                          :text-field-props="{
                            appendIcon: 'mdi-calendar-check',
                            outlined: true,
                            dense: true,
                            placeholder: 'Selecione una fecha',
                            rules: this.rules.fechaCita,
                            required: true
                          }"
                          :time-picker-props="{
                            min: this.dentista.horaInicio,
                            max: this.dentista.horaFin,
                            'allowed-minutes': this.allowedMinutes
                          }"
                          :date-picker-props="{
                            min: new Date().toISOString().slice(0, 10)
                          }"
                          date-format="MM/dd/yyyy"
                          :disabled="!editable"
                        >
                          <template slot="dateIcon">
                            <v-icon>mdi-calendar</v-icon>
                          </template>
                          <template slot="timeIcon">
                            <v-icon>mdi-clock</v-icon>
                          </template>
                          <template slot="actions" slot-scope="{ parent }">
                            <v-btn text @click.native="parent.clearHandler"
                              >Cancelar</v-btn
                            >
                            <v-btn text @click="parent.okHandler"
                              >Confirmar</v-btn
                            >
                          </template>
                        </v-datetime-picker>
                      </v-col>
                    </v-row>
                  </v-form>

                  <v-col cols="12">
                    <v-tooltip bottom>
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                          v-if="!citaSeleccionada.pagada"
                          icon
                          @click="abriDialogoPago"
                        >
                          <v-icon color="green" dark v-bind="attrs" v-on="on">
                            mdi-credit-card-outline
                          </v-icon>
                        </v-btn>
                        <v-btn v-else icon>
                          <v-icon color="gray" dark v-bind="attrs" v-on="on">
                            mdi-credit-card-outline
                          </v-icon>
                        </v-btn>
                      </template>
                      <span v-if="!citaSeleccionada.pagada">Pagar cita</span>
                      <span v-else>Cita pagada</span>
                    </v-tooltip>
                    <v-tooltip bottom v-if="user.role == 'Dentista'">
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                          v-if="!citaSeleccionada.tratamiento"
                          icon
                          @click="abriDialogoTratamientos"
                        >
                          <v-icon color="blue" dark v-bind="attrs" v-on="on">
                            mdi-human
                          </v-icon>
                        </v-btn>
                        <v-btn v-else icon>
                          <v-icon
                            color="yellow"
                            dark
                            v-bind="attrs"
                            v-on="on"
                            @click="abriDialogoEditarTratamiento"
                          >
                            mdi-human
                          </v-icon>
                        </v-btn>
                      </template>
                      <span v-if="!citaSeleccionada.tratamiento"
                        >Administar tratamiento</span
                      >
                      <span v-else>Editar tratamiento</span>
                    </v-tooltip>
                  </v-col>
                </v-card-text>
                <v-card-actions>
                  <v-btn
                    text
                    color="primary"
                    v-if="editable"
                    @click="actualizar"
                  >
                    Guardar
                  </v-btn>
                  <v-spacer></v-spacer>
                  <v-btn text color="primary" @click="selectedOpen = false">
                    Cerrar
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-menu>
          </v-sheet>
        </v-col>
      </v-row>
    </v-card-text>
    <v-dialog v-model="dialogDelete" max-width="500px" style="z-index:207">
      <v-card>
        <v-card-title class="headline"
          >¿Estas seguro que deseas cancelar <br />
          esta cita?</v-card-title
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialogDelete = false"
            >No</v-btn
          >
          <v-btn color="blue darken-1" text @click="cancelarCita">Si</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <div
      role="document"
      tabindex="0"
      data-v-55ef5c75=""
      class="v-dialog__content v-dialog__content--active"
      :style="{ 'z-index': dialogPago ? 207 : 203 }"
      v-show="dialogPago"
    >
      <div
        class="v-dialog v-dialog--active v-dialog--persistent"
        style="transform-origin: center center; max-width: 500px;"
      >
        <v-card>
          <v-toolbar color="azulSecundario" dark flat>
            <span class="headline">Pagar cita</span>
            <v-spacer></v-spacer>
            <v-btn icon @click="cerrarDialogoPagar">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
          <v-card-text>
            <v-container>
              <v-form ref="formPago">
                <v-row>
                  <v-col cols="12">
                    <h2>Seleccione el metodo de pago</h2>
                  </v-col>
                  <v-col cols="12">
                    <v-radio-group v-model="metodo" row>
                      <v-radio
                        v-if="user.role != 'Paciente'"
                        label="Pago en efectivo"
                        value="1"
                      ></v-radio>
                      <v-radio label="Pago en linea" value="2"> </v-radio>
                    </v-radio-group>
                  </v-col>
                  <v-col cols="12">
                    <h2>Servicios:</h2>
                    <v-divider></v-divider>
                    <v-row v-for="(item, i) in serviciospago" v-bind:key="i">
                      <v-col :cols="item.costoXDiente ? '4' : '6'">
                        <v-text-field
                          label="Servicio"
                          :value="item.nombre"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="4" v-if="item.costoXDiente">
                        <v-text-field
                          label="Dientes"
                          type="number"
                          v-model="item.dientes"
                          placeholder="numero de dientes"
                          outlined
                          dense
                          required
                          :rules="rules.dientes"
                          @input="calcularReal(item)"
                        ></v-text-field>
                      </v-col>
                      <v-col :cols="item.costoXDiente ? '4' : '6'">
                        <v-text-field
                          label="Precio"
                          v-model="item.real"
                          outlined
                          dense
                          readonly
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-col>
                  <v-col cols="12">
                    <h2>Total: ${{ total }}</h2>
                  </v-col>
                  <v-col cols="6" v-if="metodo === '1'">
                    <v-text-field
                      label="Pago con"
                      v-model="pagocon"
                      dense
                      type="number"
                      outlined
                      placeholder="$ pesos"
                      :rules="[
                        val =>
                          val === 0 ||
                          (val && !isNaN(val)) ||
                          'la cantidad a pagar es requeirida',
                        val =>
                          val >= this.total ||
                          'la cantidad a pagar es inferior al total'
                      ]"
                      required
                    >
                    </v-text-field>
                  </v-col>
                  <v-col cols="6" v-if="metodo === '1'">
                    <v-text-field
                      label="Cambio"
                      outlined
                      readonly
                      type="number"
                      :value="pagocon - total < 0 ? '' : pagocon - total"
                      dense
                      placeholder="$ pesos"
                    >
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-form>
              <div v-show="metodo === '2'" class="container">
                <div class="row text-center">
                  <!-- <div class="w-100 text-center">
                    <h5 class="mb-3 text-center">
                      Your domain successfully registered, but you need to pay
                      to be displayed on our website!
                    </h5>
                    <h5 class="mb-3 text-center">Please pay $4.99</h5>
                  </div> -->

                  <div style="display: contents;">
                    <div class="mx-auto w-50" ref="paypal"></div>
                  </div>
                </div>
              </div>
            </v-container>
          </v-card-text>
          <v-card-actions v-if="metodo === '1'">
            <v-spacer></v-spacer>
            <v-btn @click="guardarPago" color="primary" text
              >Confirmar pago</v-btn
            >
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </div>
    </div>
    <v-dialog v-model="dialogTratamiento" style="z-index:207">
      <v-card>
        <v-toolbar color="azulSecundario" dark flat>
          <span class="headline">Tratamiento</span>
          <v-spacer></v-spacer>
          <v-btn icon @click="cerrarDialogoTratamiento">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text>
          <v-stepper v-model="e1">
            <v-stepper-header>
              <span
                style="display: contents;"
                v-for="(item, i) in serviciostratamiento"
                v-bind:key="i"
              >
                <v-stepper-step :complete="e1 > i + 1" :step="i + 1">
                  {{ item.servicio.nombre }}
                </v-stepper-step>
                <v-divider></v-divider>
              </span>
              <v-stepper-step
                :complete="e1 > serviciostratamiento.length + 1"
                :step="serviciostratamiento.length + 1"
              >
                Odontograma
              </v-stepper-step>
            </v-stepper-header>
            <v-stepper-items>
              <v-stepper-content
                v-for="(item, i) in serviciostratamiento"
                v-bind:key="i"
                :step="i + 1"
              >
                <v-card flat>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <v-textarea
                          outlined
                          label="Sintomas"
                          placeholder="Escribir los sintomas del paciente..."
                          auto-grow
                          v-model="item.sintomas"
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12">
                        <v-textarea
                          outlined
                          label="Observacion"
                          placeholder="Escribir los las observaciones del paciente..."
                          auto-grow
                          v-model="item.observacion"
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12">
                        <v-textarea
                          outlined
                          label="Receta"
                          placeholder="Escribir la receta o recomendacione para el paciente..."
                          auto-grow
                          v-model="item.receta"
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12">
                        <v-file-input
                          centered
                          truncate-length="18"
                          accept="image/*"
                          label="Cargar Evidencia"
                          placeholder="Ejemplo radiografia o fotografia"
                          dense
                          outlined
                          v-model="item.imagen"
                          @change="cargarEvidencia(item)"
                        ></v-file-input>
                      </v-col>
                      <v-col cols="12" v-if="item.evidencia">
                        <v-img
                          class="mx-auto"
                          :src="item.evidencia"
                          contain
                          width="400px"
                        ></v-img>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </v-card>

                <v-btn color="primary" @click="e1++">
                  Continuar
                </v-btn>

                <v-btn text @click="e1 > 1 ? e1-- : e1">
                  Regresar
                </v-btn>
              </v-stepper-content>
              <v-stepper-content :step="serviciostratamiento.length + 1">
                <v-card flat>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <div
                          class="diente"
                          v-for="(item, i) in dientesSuperior"
                          v-bind:key="i"
                        >
                          <!--item 1-->
                          <input name="valor" type="hidden" value="1" />
                          <div
                            class="cuadro click"
                            @click="cambiarEstado(1, item)"
                            :style="[
                              item.norte == 1
                                ? { background: 'red' }
                                : item.norte == 2
                                ? { background: 'blue' }
                                : item.norte == 3
                                ? { background: 'yellow' }
                                : item.norte == 4
                                ? { background: 'tomato' }
                                : item.norte == 5
                                ? { background: 'brown' }
                                : item.norte == 6
                                ? { background: 'blueviolet' }
                                : item.norte == 7
                                ? { background: 'green' }
                                : item.norte == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="1" />
                          </div>
                          <div
                            class="cuadro izquierdo click"
                            @click="cambiarEstado(2, item)"
                            :style="[
                              item.izquierdo == 1
                                ? { background: 'red' }
                                : item.izquierdo == 2
                                ? { background: 'blue' }
                                : item.izquierdo == 3
                                ? { background: 'yellow' }
                                : item.izquierdo == 4
                                ? { background: 'tomato' }
                                : item.izquierdo == 5
                                ? { background: 'brown' }
                                : item.izquierdo == 6
                                ? { background: 'blueviolet' }
                                : item.izquierdo == 7
                                ? { background: 'green' }
                                : item.izquierdo == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="2" />
                          </div>
                          <div
                            class="cuadro debajo click"
                            @click="cambiarEstado(3, item)"
                            :style="[
                              item.sur == 1
                                ? { background: 'red' }
                                : item.sur == 2
                                ? { background: 'blue' }
                                : item.sur == 3
                                ? { background: 'yellow' }
                                : item.sur == 4
                                ? { background: 'tomato' }
                                : item.sur == 5
                                ? { background: 'brown' }
                                : item.sur == 6
                                ? { background: 'blueviolet' }
                                : item.sur == 7
                                ? { background: 'green' }
                                : item.sur == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="3" />
                          </div>
                          <div
                            class="cuadro derecha click"
                            @click="cambiarEstado(4, item)"
                            :style="[
                              item.derecho == 1
                                ? { background: 'red' }
                                : item.derecho == 2
                                ? { background: 'blue' }
                                : item.derecho == 3
                                ? { background: 'yellow' }
                                : item.derecho == 4
                                ? { background: 'tomato' }
                                : item.derecho == 5
                                ? { background: 'brown' }
                                : item.derecho == 6
                                ? { background: 'blueviolet' }
                                : item.derecho == 7
                                ? { background: 'green' }
                                : item.derecho == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="4" />
                          </div>
                          <div
                            class="centro click"
                            @click="cambiarEstado(5, item)"
                            :style="[
                              item.centro == 1
                                ? { background: 'red' }
                                : item.centro == 2
                                ? { background: 'blue' }
                                : item.centro == 3
                                ? { background: 'yellow' }
                                : item.centro == 4
                                ? { background: 'tomato' }
                                : item.centro == 5
                                ? { background: 'brown' }
                                : item.centro == 6
                                ? { background: 'blueviolet' }
                                : item.centro == 7
                                ? { background: 'green' }
                                : item.centro == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="5" />
                          </div>
                        </div>
                      </v-col>
                      <v-col cols="12">
                        <div
                          class="diente"
                          v-for="(item, i) in dientesInferior"
                          v-bind:key="i"
                        >
                          <!--item 1-->
                          <input name="valor" type="hidden" value="1" />
                          <div
                            class="cuadro click"
                            @click="cambiarEstado(1, item)"
                            :style="[
                              item.norte == 1
                                ? { background: 'red' }
                                : item.norte == 2
                                ? { background: 'blue' }
                                : item.norte == 3
                                ? { background: 'yellow' }
                                : item.norte == 4
                                ? { background: 'tomato' }
                                : item.norte == 5
                                ? { background: 'brown' }
                                : item.norte == 6
                                ? { background: 'blueviolet' }
                                : item.norte == 7
                                ? { background: 'green' }
                                : item.norte == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="1" />
                          </div>
                          <div
                            class="cuadro izquierdo click"
                            @click="cambiarEstado(2, item)"
                            :style="[
                              item.izquierdo == 1
                                ? { background: 'red' }
                                : item.izquierdo == 2
                                ? { background: 'blue' }
                                : item.izquierdo == 3
                                ? { background: 'yellow' }
                                : item.izquierdo == 4
                                ? { background: 'tomato' }
                                : item.izquierdo == 5
                                ? { background: 'brown' }
                                : item.izquierdo == 6
                                ? { background: 'blueviolet' }
                                : item.izquierdo == 7
                                ? { background: 'green' }
                                : item.izquierdo == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="2" />
                          </div>
                          <div
                            class="cuadro debajo click"
                            @click="cambiarEstado(3, item)"
                            :style="[
                              item.sur == 1
                                ? { background: 'red' }
                                : item.sur == 2
                                ? { background: 'blue' }
                                : item.sur == 3
                                ? { background: 'yellow' }
                                : item.sur == 4
                                ? { background: 'tomato' }
                                : item.sur == 5
                                ? { background: 'brown' }
                                : item.sur == 6
                                ? { background: 'blueviolet' }
                                : item.sur == 7
                                ? { background: 'green' }
                                : item.sur == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="3" />
                          </div>
                          <div
                            class="cuadro derecha click"
                            @click="cambiarEstado(4, item)"
                            :style="[
                              item.derecho == 1
                                ? { background: 'red' }
                                : item.derecho == 2
                                ? { background: 'blue' }
                                : item.derecho == 3
                                ? { background: 'yellow' }
                                : item.derecho == 4
                                ? { background: 'tomato' }
                                : item.derecho == 5
                                ? { background: 'brown' }
                                : item.derecho == 6
                                ? { background: 'blueviolet' }
                                : item.derecho == 7
                                ? { background: 'green' }
                                : item.derecho == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="4" />
                          </div>
                          <div
                            class="centro click"
                            @click="cambiarEstado(5, item)"
                            :style="[
                              item.centro == 1
                                ? { background: 'red' }
                                : item.centro == 2
                                ? { background: 'blue' }
                                : item.centro == 3
                                ? { background: 'yellow' }
                                : item.centro == 4
                                ? { background: 'tomato' }
                                : item.centro == 5
                                ? { background: 'brown' }
                                : item.centro == 6
                                ? { background: 'blueviolet' }
                                : item.centro == 7
                                ? { background: 'green' }
                                : item.centro == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="5" />
                          </div>
                        </div>
                      </v-col>
                    </v-row>
                  </v-card-text>
                  <v-card-actions>
                    <v-btn color="#FF0000" @click="estadoDiente = 1" dark
                      >Caries</v-btn
                    >
                    <v-btn color="#0000FF" @click="estadoDiente = 2" dark
                      >Amalgama</v-btn
                    >
                    <v-btn color="#FFFF00" @click="estadoDiente = 3" light
                      >Endodoncia</v-btn
                    >
                    <v-btn color="#FF6347" @click="estadoDiente = 4" dark
                      >Ausente</v-btn
                    >
                    <v-btn color="#A52A2A" @click="estadoDiente = 5" dark
                      >Resina</v-btn
                    >
                    <v-btn color="#8A2BE2" @click="estadoDiente = 6" dark
                      >Implante</v-btn
                    >
                    <v-btn color="#008000" @click="estadoDiente = 7" dark
                      >Sellante</v-btn
                    >
                    <v-btn color="#FFA500" @click="estadoDiente = 8" dark
                      >Corona</v-btn
                    >
                    <v-btn color="white" light @click="estadoDiente = 0"
                      >Normal</v-btn
                    >
                  </v-card-actions>
                </v-card>

                <v-btn color="primary" @click="validarTratamiento">
                  Guardar
                </v-btn>

                <v-btn text @click="e1 > 1 ? e1-- : e1">
                  Regresar
                </v-btn>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-card>
</template>
<script>
import { mapState } from "vuex";
import axios from "axios";
import { environment } from "../env/environment";
import moment from "moment";
var paypalAction;
export default {
  data: () => ({
    e1: 1,
    loaded: false,
    dientesSuperior: [],
    dientesInferior: [],
    serviciostratamiento: [{ servicio: { nombre: "" } }],
    serviciospago: [{ real: 0 }],
    metodo: "1",
    total: 0,
    pagocon: 0,
    cambio: 0,
    dialogPago: false,
    dialogTratamiento: false,
    tratamiento: {},
    focus: "",

    type: "month",
    typeToLabel: {
      month: "Mes",
      week: "Semana",
      day: "Dia",
      "4day": "4 Dias"
    },
    citaSeleccionada: {
      id: "",
      paciente: {
        id: "",
        nombre: "",
        apellidoPat: "",
        apellidoMat: "",
        nombreUsuario: "",
        contrasenia: "",
        email: "",
        role: "",
        telefono: "",
        foto: "",
        consultorio: "",
        datosConsultorio: {
          id: "",
          nombre: "",
          telefonos: [
            {
              id: 0,
              tel: ""
            }
          ],
          direccionConsultorio: {
            calle: "",
            numero: "",
            depto: "",
            colonia: ""
          }
        },
        horaInicio: "",
        horaFin: ""
      },
      dentista: {
        id: "",
        nombre: "",
        apellidoPat: "",
        apellidoMat: "",
        nombreUsuario: "",
        contrasenia: "",
        email: "",
        role: "",
        telefono: "",
        foto: "",
        consultorio: "",
        datosConsultorio: {
          id: "",
          nombre: "",
          telefonos: [
            {
              id: 0,
              tel: ""
            }
          ],
          direccionConsultorio: {
            calle: "",
            numero: "",
            depto: "",
            colonia: ""
          }
        },
        horaInicio: "",
        horaFin: ""
      },
      fechaCita: "",
      fechaRegistro: "",
      servicios: [
        {
          id: "",
          nombre: "",
          precio: 0,
          descripcion: "",
          consultorio: ""
        }
      ],
      status: "",
      pagada: false
    },
    selectedElement: null,
    selectedOpen: false,
    events: [],
    colors: [
      "blue",
      "indigo",
      "deep-purple",
      "cyan",
      "green",
      "orange",
      "grey darken-1"
    ],
    fechaCita: null,
    consultorios: [],
    consultorio: null,
    dentista: {},
    dentistas: [],
    servicioscita: [],
    servicios: [],
    estadoDiente: 1,
    nombrepaciente: "",
    paciente: {},
    pacientes: [],
    actionStatus: {},
    editable: false,
    dialogDelete: false,
    rules: {
      fechaCita: [val => (val || "").length > 0 || "La hora es requerida"],
      dentista: [
        val => (val.id || "").length > 0 || " El dentista es requerido"
      ],
      paciente: [val => (val || "").length > 0 || " El paciente es requerido"],
      consultorio: [
        val => (val || "").length > 0 || "El consultorio es requeirido"
      ],
      servicioscita: [val => val.length > 0 || " Los servicio son requeridos"],
      dientes: [
        val =>
          val === 0 ||
          (val && !isNaN(val)) ||
          "la cantidad de dientes es requeirida",
        val =>
          (val > 0 && val <= 32) ||
          "La cantidad de dientes no esta en el rango valido"
      ]
    }
  }),
  created() {
    this.initialize();
  },
  computed: {
    ...mapState(["user"])
  },
  mounted() {
    const script = document.createElement("script");
    script.src =
      "https://www.paypal.com/sdk/js?client-id=AQrEbuJH2YEq3j1dRmmw8JeyP9r11LnimLKwyVB97JZ94Fz1wDFo7dv78vo7M4zjEr3mgH-1xMmMiVtJ&currency=MXN";
    script.addEventListener("load", this.setLoaded);
    document.body.appendChild(script);
  },
  methods: {
    setLoaded: function() {
      this.loaded = true;

      //var pagoValidation = this.$refs.formPago;
      window.paypal
        .Buttons({
          style: {
            color: "gold",
            shape: "pill",
            label: "pay",
            size: "responsive",
            fundingicons: "true"
          },
          onInit: function(data, actions) {
            //actions.disable();
            paypalAction = actions;
          },
          createOrder: (data, actions) => {
            return actions.order.create({
              purchase_units: [
                {
                  description: "Pago de servicios",
                  amount: {
                    currency_code: "MXN",
                    value: this.total
                  }
                }
              ]
            });
          },
          onApprove: async (data, actions) => {
            this.$vloading.show();
            await actions.order.capture();
            this.$vloading.hide();
            //window.location.href = "./paymentsuccess/" +this.resp;
            //actionStatus.disable();
            let pago = {
              monto: this.total,
              idUsuario: this.user.id,
              metodo: 2,
              cita: this.citaSeleccionada
            };
            this.pagarCita(pago);
          }
        })
        .render(this.$refs.paypal);
    },
    pagarCita(pago) {
      this.$vloading.show();
      axios
        .post(environment.url + "pago", pago)
        .then(() => {
          //this.dentistas = res.data;
          this.$vloading.hide();
          this.$toast.success("Pago realizado correctamente");
          this.limpiarDialogoPagar();
          this.dialogPago = false;
          this.citaSeleccionada.pagada = true;
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo guardar el pago");
        });
    },
    guardarTratamiento(tratamiento) {
      this.$vloading.show();
      axios
        .post(environment.url + "tratamiento", tratamiento)
        .then(res => {
          //this.dentistas = res.data;
          this.$vloading.hide();
          this.$toast.success("Se guardo correctamente el tratamiento");
          this.dialogTratamiento = false;
          this.citaSeleccionada.tratamiento = res.data.id;
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo guardar el tratamiento");
        });
    },
    actualizarTratamiento(tratamiento) {
      this.$vloading.show();
      axios
        .put(environment.url + "tratamiento", tratamiento)
        .then(() => {
          //this.dentistas = res.data;
          this.$vloading.hide();
          this.$toast.success("Se actualizo correctamente el tratamiento");
          this.tratamiento = {};
          this.dialogTratamiento = false;
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo actualizar el tratamiento");
        });
    },
    recargarDatos() {
      for (let dentista of this.dentistas) {
        if (dentista.id == this.citaSeleccionada.dentista.id)
          this.dentista = dentista;
      }
    },
    obtenerDentistas() {
      this.$vloading.show();
      axios
        .get(environment.url + "usuario/dentista/" + this.consultorio)
        .then(res => {
          this.dentistas = res.data;
          this.recargarDatos();
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los Dentistas");
        });
    },
    obtenerConsultorios() {
      this.$vloading.show();
      axios
        .get(environment.url + "consultorio")
        .then(response => {
          this.consultorios = response.data;
          if (this.consultorios.length == 0) {
            this.$toast.info("No hay registros consultorios");
          }
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los consultorios");
        });
    },
    obtenerPacientes() {
      this.$vloading.show();
      axios
        .get(environment.url + "usuario/role/Paciente")
        .then(response => {
          this.pacientes = response.data;
          if (this.pacientes.length == 0) {
            this.$toast.info("No hay registros Pacientes");
          }
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los Pacientes");
        });
    },
    obtenerServicios() {
      this.$vloading.show();
      axios
        .get(environment.url + "servicio/consultorio/" + this.consultorio)
        .then(response => {
          this.servicios = response.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener servicios");
        });
    },
    cargarDatosConsultorio() {
      this.obtenerServicios();
      this.obtenerDentistas();
    },
    limpiarDialogoPagar() {
      this.serviciospago = [];
      this.metodo = "1";
      this.total = 0;
      this.pagocon = 0;
      this.cambio = 0;
    },
    cerrarDialogoTratamiento() {
      this.dialogTratamiento = false;
    },
    cerrarDialogoPagar() {
      this.$refs.formPago.resetValidation();
      this.limpiarDialogoPagar();
      this.dialogPago = false;
      //this.selectedOpen = true;
    },
    calcularReal(item) {
      //
      item.real = item.precio * item.dientes;
      this.total = 0;
      this.serviciospago.forEach(element => {
        this.total += element.real;
      });
      if (this.metodo == "2") {
        if (!this.$refs.formPago.validate()) {
          paypalAction.disable();
          //
        } else {
          paypalAction.enable();
        }
      }
    },
    actualizarCita(cita) {
      axios
        .post(environment.url + "cita/disponible", cita)
        .then(response => {
          this.$vloading.hide();
          if (response.data.status === 1) {
            axios
              .put(environment.url + "cita", cita)
              .then(() => {
                this.$vloading.hide();
                this.selectedOpen = false;
                this.initialize();
                this.$toast.success("Se actualizo el cita correctamente");
              })
              .catch(() => {
                this.$vloading.hide();
                this.$toast.error("Error: No se pudo actualizar la cita ");
              });
          } else {
            this.$toast.warning("¡la fecha que elegiste no esta disponible!");
          }
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error no se pudo verificar la disponibilidad");
        });
    },
    cancelarCita() {
      this.$vloading.show();
      axios
        .delete(environment.url + "cita/" + this.citaSeleccionada.id)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se cancelo la cita correctamente");
          this.initialize();
          this.citaSeleccionada.status = "Cancelada";
          this.dialogDelete = false;
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pude cancelar la cita");
        });
    },
    allowedMinutes: v => v % 30 == 0,
    obtenerCitas() {
      this.$vloading.show();
      let ruta = environment.url + "cita";
      if (this.user.role == "Dentista") ruta += "/dentista/" + this.user.id;
      else if (this.user.role == "Paciente")
        ruta += "/paciente/" + this.user.id;
      axios
        .get(ruta)
        .then(res => {
          for (let cita of res.data) {
            cita.start = moment(cita.fechaCita)
              .utc()
              .format("YYYY-MM-DD H:mm:ss");
            cita.end = moment(cita.fechaCita)
              .utc()
              .add(1, "h")
              .format("YYYY-MM-DD H:mm:ss");
          }
          this.events = res.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener las citas");
        });
    },
    viewDay({ date }) {
      this.focus = date;
      this.type = "day";
    },
    getEventColor(event) {
      return event.color;
    },
    getNombre(event) {
      return "Cita dental de:" + event.input.paciente.nombre;
    },
    setToday() {
      this.focus = "";
    },
    prev() {
      this.$refs.calendar.prev();
    },
    next() {
      this.$refs.calendar.next();
    },
    showEvent({ nativeEvent, event }) {
      const open = () => {
        this.citaSeleccionada = Object.assign({}, event);
        this.nombrepaciente =
          event.paciente.nombre +
          " " +
          event.paciente.apellidoPat +
          " " +
          event.paciente.apellidoMat;
        this.selectedElement = nativeEvent.target;
        this.consultorio = event.dentista.consultorio;
        this.paciente = event.paciente;
        this.dentista = event.dentista;
        this.servicioscita = event.servicios;

        this.cargarDatosConsultorio();
        this.fechaCita = new Date(event.start);
        this.editable = false;
        setTimeout(() => {
          this.selectedOpen = true;
        }, 10);
      };

      if (this.selectedOpen) {
        this.selectedOpen = false;
        setTimeout(open, 10);
      } else {
        open();
      }

      nativeEvent.stopPropagation();
    },
    updateRange() {},
    initialize() {
      this.obtenerCitas();
      if (this.user.role != "Dentista") this.obtenerConsultorios();
    },
    cargarEvidencia(item) {
      console.log(item.imagen);
      if (item.imagen !== undefined) {
        var reader = new FileReader();
        reader.readAsDataURL(item.imagen);
        reader.onloadend = function() {
          item.evidencia = reader.result;
        };
      } else {
        item.evidencia = "";
      }
    },
    abriDialogoTratamientos() {
      this.serviciostratamiento = [];
      let detalleTratamiento = {};
      for (let i = 0; i < this.servicioscita.length; i++) {
        detalleTratamiento = {
          servicio: this.servicioscita[i],
          observacion: "",
          sintomas: "",
          imagen: null,
          evidencia: "",
          receta: ""
        };
        this.serviciostratamiento.push(detalleTratamiento);
      }
      this.estadoDiente = 1;
      this.dientesSuperior = [];
      this.dientesInferior = [];
      let diente = {};
      for (let i = 0; i < 16; i++) {
        diente = {
          izquierdo: 0,
          derecho: 0,
          centro: 0,
          norte: 0,
          sur: 0,
          estado: 0
        };
        this.dientesSuperior.push(diente);
      }
      for (let i = 0; i < 16; i++) {
        diente = {
          izquierdo: 0,
          derecho: 0,
          centro: 0,
          norte: 0,
          sur: 0,
          estado: 0
        };
        this.dientesInferior.push(diente);
      }
      this.dialogTratamiento = true;
      this.e1 = 1;
    },
    cambiarEstado(posicion, diente) {
      if (diente.estado > 0) {
        diente.izquierdo = 0;
        diente.derecho = 0;
        diente.centro = 0;
        diente.norte = 0;
        diente.sur = 0;
        diente.estado = 0;
      }
      if (
        this.estadoDiente == 8 ||
        this.estadoDiente == 6 ||
        this.estadoDiente == 3 ||
        this.estadoDiente == 4
      ) {
        diente.izquierdo = this.estadoDiente;
        diente.derecho = this.estadoDiente;
        diente.centro = this.estadoDiente;
        diente.norte = this.estadoDiente;
        diente.sur = this.estadoDiente;
        diente.estado = this.estadoDiente;
      } else {
        switch (posicion) {
          case 1:
            diente.norte = this.estadoDiente;
            break;
          case 2:
            diente.izquierdo = this.estadoDiente;
            break;
          case 3:
            diente.sur = this.estadoDiente;
            break;
          case 4:
            diente.derecho = this.estadoDiente;
            break;
          default:
            diente.centro = this.estadoDiente;
        }
      }
    },
    abriDialogoPago() {
      //this.selectedOpen = false;
      //paypalAction.disable();
      if (this.user.role == "Paciente") this.metodo = "2";
      this.serviciospago = JSON.parse(JSON.stringify(this.servicioscita));
      this.serviciospago.forEach(item => {
        this.$set(item, "real", item.precio);
        this.$set(item, "dientes", 1);
        this.total += item.precio;
      });
      this.dialogPago = true;
    },
    guardarPago() {
      if (this.$refs.formPago.validate()) {
        let pago = {
          monto: this.total,
          idUsuario: this.user.id,
          metodo: 1,
          cita: this.citaSeleccionada
        };

        this.pagarCita(pago);
      }
    },
    actualizar() {
      if (this.$refs.form.validate()) {
        let cita = {
          id: this.citaSeleccionada.id,
          paciente: this.paciente,
          dentista: this.dentista,
          fechaCita: new Date(this.fechaCita + "UTC").toISOString(),
          servicios: this.servicioscita,
          status: "Pendiente",
          pagada: this.citaSeleccionada.pagada,
          tratamiento: this.citaSeleccionada.tratamiento
        };
        this.actualizarCita(cita);
      }
    },
    validarTratamiento() {
      if (this.tratamiento.id) {
        this.actualizarTratamiento(this.tratamiento);
        return;
      }
      let tratamiento = {
        cita: this.citaSeleccionada,
        odontograma: {
          dientesSuperior: this.dientesSuperior,
          dientesInferior: this.dientesInferior
        },
        detalle: this.serviciostratamiento
      };
      this.guardarTratamiento(tratamiento);
    },
    abriDialogoEditarTratamiento() {
      this.$vloading.show();
      axios
        .get(
          environment.url + "tratamiento/" + this.citaSeleccionada.tratamiento
        )
        .then(res => {
          this.$vloading.hide();
          this.tratamiento = res.data;
          this.estadoDiente = 1;
          this.dientesSuperior = res.data.odontograma.dientesSuperior;
          this.dientesInferior = res.data.odontograma.dientesInferior;
          this.serviciostratamiento = res.data.detalle;
          this.dialogTratamiento = true;
          this.e1 = 1;
        })
        .catch(() => {
          this.$toast.error("No se pudo obtener el tratamiento");
          this.$vloading.hide();
        });
    }
  }
};
</script>
<style lang="scss">
.compact-form {
  transform: scale(0.875);
  transform-origin: center;
}
.calendar {
  overflow-x: auto;
}
.v-select.v-input--dense .v-chip {
  margin: 4px !important;
}
.diente {
  width: 0px;
  height: auto;
  margin-left: 50px;
  display: inline-block;
}

.cuadro {
  background-color: #ffffff;
  border: 1px solid #ccc;
  position: relative;
  width: 20px;
  height: 15px;
  left: 45px;
  -webkit-border-radius: 80px 80px 0px 15px;
  -moz-border-radius: 80px 80px 0px 15px;
  border-radius: 80px 80px 0px 15px;
}
.cuadro:hover {
  background: rgba(117, 198, 243, 0.4);
  cursor: pointer;
}
.izquierdo {
  top: 1px !important;
  left: 29px !important;
  -webkit-transform: rotate(270deg);
  -moz-transform: rotate(270deg);
  -ms-transform: rotate(270deg);
  -o-transform: rotate(270deg);
  transform: rotate(270deg);
}
.debajo {
  top: 1px !important;
  left: 45px !important;
  -webkit-transform: rotate(180deg);
  -moz-transform: rotate(180deg);
  -ms-transform: rotate(180deg);
  -o-transform: rotate(180deg);
  transform: rotate(180deg);
}
.derecha {
  top: -29px !important;
  left: 61px !important;
  -webkit-transform: rotate(90deg);
  -moz-transform: rotate(90deg);
  -ms-transform: rotate(90deg);
  -o-transform: rotate(90deg);
  transform: rotate(90deg);
}
.centro {
  background: #f3f3f3;
  border: 1px solid #ccc;
  width: 20px;
  height: 18px;
  top: -46px;
  left: 45px;
  position: relative;
}
.centro:hover {
  border: 1px solid rgba(117, 198, 243, 0.4);
  background-color: rgba(117, 198, 243, 0.4);
  cursor: pointer;
}
.color {
  width: 200px;
  height: 200px;
  position: relative;
  margin: 0 auto;
  padding: 3px;
}
.select {
  width: 40px;
  height: 40px;
  margin: 2px;
  background-color: #ccc;
  display: inline-block;
  cursor: pointer;
}
.select:hover {
  border: 1px solid rgba(117, 198, 243, 0.4);
}
.content {
  position: absolute;
}
.ui-dialog {
  position: relative !important;
}
</style>
