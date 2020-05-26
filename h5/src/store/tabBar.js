const tabBar = {
    state: {
        active:"home",
    },
    mutations: {
        setActive(state, active) {
            state.active = active
        },
    },
    namespaced: true
};

export default tabBar;
