import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    routes: [
        {
            // ======================
            // Full Layout
            // ======================
            path: '',
            component: () => import('./layout/full/MainContainer.vue'),
            // ======================
            // Theme routes / pages
            // ======================

            children: [
                {
                    path: '/',
                    redirect: '/Projects'
                },

                {
                    path: '/Projects',
                    name: 'Projects',
                    index: 1,
                    component: () => import('./views/components/pe2/Projects')
                },
                {
                    path:'/registrations',
                    name: 'Registrations',
                    index: 3,
                    component: () => import('./views/components/pe2/registrations')
                },

                {
                    path: '/submittedRegistrations',
                    name: 'Submitted Registration',
                    index: 4,
                    component: () => import('./views/components/pe2/submittedRegistrations')
                },
                {
                    path:'/registrationsEdit/:registration',
                    name: 'Registrations Edit',
                    index: 7,
                    component: () => import('./views/components/pe2/registrationsEdit')
                },
                {
                    path: '/teams',
                    name: 'Teams',
                    index: 5,
                    component: () => import('./views/components/pe2/Teams.vue')
                },
                {
                    path: '/teamsEdit/:team',
                    name: 'Teams Edit',
                    index: 6,
                    component: () => import('./views/components/pe2/TeamsEdit')
                },



            ]
        },
        // Redirect to starterkit page, if no match found
        {
            path: '*',
            redirect: '/starterkit'
        }
    ]
})