db.createUser(
  {
    user: 'api_user',
    pwd: 'NdEep0XLpMNKUmgQVa81oDCx7mrSRodh0Z79qdX3',
    roles: [
		{ role: 'readWrite', db: 'customer' },
		{ role: 'readWrite', db: 'user' }
	],
  },
);
db.createCollection('customer');
db.createCollection('user');

db.user.insertOne(
  {
    _id: "1",
    username: 'admin',
    password: '$2a$10$8R82VQODT8PF6YNPqaJ6veJ7hw.MvH/rZFX0TKoyaK874wsvGNi3y', // agilemonkeysadmin,
    name: 'Admin',
    surname: 'Agile monkeys',
    userType: 'admin',
    userStatus: 'active'
  }
);