import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store";
Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Login",
    component: () =>
      import(/* webpackChunkName: "login" */ "../views/Login.vue")
  },
  {
    path: "/inicio",
    name: "Home",
    component: () => import(/* webpackChunkName: "home" */ "../views/Home.vue"),
    meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
  },
  {
    path: "/productos",
    name: "Productos",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "productos" */ "../views/Productos.vue"),
    meta: { allow: ["ADMIN", "Dentista"] }
  },
  {
    path: "/citas",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Citas.vue"),
    meta: { allow: ["ADMIN", "Dentista", "Paciente"] },
    children: [
      {
        path: "nueva",
        component: () =>
          import(/* webpackChunkName: "citas" */ "../views/AgendarCita.vue"),
        meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
      },
      {
        path: "proximas",
        component: () =>
          import(/* webpackChunkName: "citas" */ "../views/ProximasCitas.vue"),
        meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
      },
      {
        path: "tratamientos",
        component: () =>
          import(/* webpackChunkName: "citas" */ "../views/Tratamientos.vue"),
        meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
      },
      {
        path: "pagos",
        component: () =>
          import(/* webpackChunkName: "citas" */ "../views/Pagos.vue"),
        meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
      }
    ]
  },
  {
    path: "/servicios",
    name: "Servicios",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Servicios.vue"),
    meta: { allow: ["ADMIN", "Dentista"] }
  },
  {
    path: "/perfil",
    name: "Perfil",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "perfil" */ "../views/Perfil.vue"),
    meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
  },
  {
    path: "/reportes",
    name: "Reportes",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Reportes.vue"),
    meta: { allow: ["ADMIN", "Dentista"] }
  },
  {
    path: "/inventario",
    name: "Inventario",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Inventario.vue"),
    meta: { allow: ["ADMIN", "Dentista"] }
  },
  {
    path: "/consultorios",
    name: "Consultorios",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Consultorios.vue"),
    meta: { allow: ["ADMIN"] }
  },
  {
    path: "/pacientes",
    name: "Pacientes",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Pacientes.vue"),
    meta: { allow: ["ADMIN", "Dentista"] }
  },
  {
    path: "/dentistas",
    name: "Dentistas",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "citas" */ "../views/Dentistas.vue"),
    meta: { allow: ["ADMIN"] }
  },
  {
    path: "/404",
    name: "404",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "citas" */ "../views/404.vue"),
    meta: { allow: ["ADMIN", "Dentista", "Paciente"] }
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});
router.beforeEach((to, from, next) => {
  if (to.path !== "/") {
    if (store.getters.isLoggedIn) {
      if (
        to.matched.some(record =>
          record.meta.allow.includes(store.getters.role)
        )
      ) {
        if (to.path === "/citas") {
          next({ path: "/citas/nueva" });
        } else {
          next();
        }
      } else {
        next({ name: "404" });
      }
    } else {
      next({ name: "Login" });
    }
  } else {
    if (store.getters.isLoggedIn) {
      next({ name: "Home" });
    } else {
      next();
    }
  }
});
export default router;
