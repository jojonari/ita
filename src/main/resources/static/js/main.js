window.ita = new Vue({
    el: '#ita',
    data: {
        app: {
            manage: {
                values: {
                    appName: '',
                    clientId: '',
                    secretKey: '',
                    partnerId: '',
                    grantType: 'code',
                    manageToken: 'refresh',
                    operationLevel: 'qa',
                    scopes: []
                },
                options: {
                    grantType: [{text: 'code', value: 'code'}, {text: 'credential', value: 'credential'}],
                    manageToken: [{text: '갱신', value: 'refresh'}, {text: '유지', value: 'keep'}],
                    operationLevel: [
                        {text: 'QA', value: 'qa'},
                        {text: 'AUTO', value: 'auto'},
                        {text: 'DEV', value: 'dev'},
                        {text: 'PROD', value: 'prod'}
                    ],
                    scopes: []
                }
            }
        },
        fields: ['first_name', 'last_name', 'age'],
        items: [
            {isActive: true, age: 40, first_name: 'Dickerson', last_name: 'Macdonald'},
            {isActive: false, age: 21, first_name: 'Larsen', last_name: 'Shaw'},
            {isActive: false, age: 89, first_name: 'Geneva', last_name: 'Wilson'},
            {isActive: true, age: 38, first_name: 'Jami', last_name: 'Carney'}
        ]
    },
    computed: {
        stateAppName() {
            return this.app.manage.values.appName.length > 3
        },
        stateClientId() {
            return this.app.manage.values.clientId.length > 16
        },
        stateSecretKey() {
            return this.app.manage.values.secretKey.length > 16
        },
        scopesStr() {
            return this.app.manage.values.scopes.join(',');
        }
    },
    created() {
        console.log('created');
    },
    mounted() {
        axios.get('/api/v1/scopes', [])
            .then(function (res) {
                ita.app.manage.options.scopes = res.data;
            }, function () {
                console.log('failed')
            })
    }
})
