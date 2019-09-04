module.exports = {
    makeStuffA: function(req, res, next) {
        req.anystuff = 'x';
        console.log("Doing awesome A stuff");
        next();
    },
    makeStuffB: function(req, res, next) {
        setTimeout(() => {
            console.log("Data " + req.anystuff);
            console.log("timeout over");
            res.send("All good man");
        }, 1000);
    }
}