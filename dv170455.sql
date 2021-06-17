
CREATE TABLE [Admin]
( 
	[korime]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[idGrad]             integer  IDENTITY  NOT NULL ,
	[naziv]              varchar(100)  NULL ,
	[postanskiBroj]      varchar(100)  NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[ime]                varchar(100)  NULL ,
	[prezime]            varchar(100)  NULL ,
	[korime]             varchar(100)  NOT NULL ,
	[sifra]              varchar(100)  NULL ,
	[brPoslatihPaketa]   integer  NULL 
)
go

CREATE TABLE [Kurir]
( 
	[korime]             varchar(100)  NOT NULL ,
	[brIsporucenihPaketa] integer  NULL ,
	[status]             integer  NULL 
	CONSTRAINT [StatusVoziNeVozi_1693638412]
		CHECK  ( status BETWEEN 0 AND 1 ),
	[profit]             decimal(10,3)  NULL ,
	[regBr]              varchar(100)  NOT NULL 
)
go

CREATE TABLE [Opstina]
( 
	[idGrad]             integer  NOT NULL ,
	[idOpstina]          integer  IDENTITY  NOT NULL ,
	[naziv]              varchar(100)  NULL ,
	[x]                  integer  NULL ,
	[y]                  integer  NULL 
)
go

CREATE TABLE [Paket]
( 
	[idPaket]            integer  IDENTITY  NOT NULL ,
	[cena]               decimal(10,3)  NULL ,
	[tip]                integer  NULL 
	CONSTRAINT [TipPaket_2058005146]
		CHECK  ( tip BETWEEN 0 AND 2 ),
	[tezina]             decimal(10,3)  NULL ,
	[status]             integer  NULL 
	CONSTRAINT [StatusPaket_687572592]
		CHECK  ( status BETWEEN 0 AND 3 ),
	[vreme]              date  NULL ,
	[korimeKurir]        varchar(100)  NULL ,
	[korimeSalje]        varchar(100)  NOT NULL ,
	[od]                 integer  NOT NULL ,
	[do]                 integer  NOT NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[idPonuda]           integer  IDENTITY  NOT NULL ,
	[procenat]           decimal(10,3)  NULL ,
	[idPaket]            integer  NOT NULL ,
	[korime]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[regBr]              varchar(100)  NOT NULL ,
	[gorivo]             integer  NULL 
	CONSTRAINT [GorivoTip_656350051]
		CHECK  ( gorivo BETWEEN 0 AND 2 ),
	[potrosnja]          decimal(10,3)  NULL 
)
go

CREATE TABLE [ZahtevPostatiKurir]
( 
	[korime]             varchar(100)  NOT NULL ,
	[regBr]              varchar(100)  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([korime] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([idGrad] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([korime] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([korime] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([idOpstina] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([idPaket] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([idPonuda] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([regBr] ASC)
go

ALTER TABLE [ZahtevPostatiKurir]
	ADD CONSTRAINT [XPKZahtevPostatiKurir] PRIMARY KEY  CLUSTERED ([korime] ASC,[regBr] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([korime]) REFERENCES [Korisnik]([korime])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([korime]) REFERENCES [Korisnik]([korime])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([regBr]) REFERENCES [Vozilo]([regBr])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([idGrad]) REFERENCES [Grad]([idGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([korimeKurir]) REFERENCES [Kurir]([korime])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([korimeSalje]) REFERENCES [Korisnik]([korime])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([od]) REFERENCES [Opstina]([idOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([do]) REFERENCES [Opstina]([idOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([idPaket]) REFERENCES [Paket]([idPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([korime]) REFERENCES [Kurir]([korime])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevPostatiKurir]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([korime]) REFERENCES [Korisnik]([korime])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevPostatiKurir]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([regBr]) REFERENCES [Vozilo]([regBr])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

create trigger TR_TransportOffer_obrisiPonude
	on Paket
	for update
	as
begin
	declare @noviStatus int
	declare @idPaket int
	declare @MyCursor Cursor

	set @MyCursor= cursor for
	select idPaket, status
	from inserted

	open @MyCursor
	fetch next from @MyCursor
	into @idPaket, @noviStatus

	while @@FETCH_STATUS = 0
	begin
		if (@noviStatus = 1)
		begin
			delete from Ponuda where idPaket = @idPaket
		end
	fetch next from @MyCursor
	into @idPaket, @noviStatus
	end
	close @MyCursor
	deallocate @MyCursor
end

create procedure spPrihvatiKurira
	@korime varchar(100),
	@regBr varchar(100),
	@ret int = 0 output
as
begin
	declare @postojiKorisnik int
	declare @postojiVozilo int

	select @postojiKorisnik = count(*) from Korisnik where korime = @korime
	select @postojiVozilo = count(*) from Vozilo where regBr = @regBr

	if (@postojiVozilo = 0 or @postojiKorisnik = 0)
	begin
		set @ret = 0
	end
	else
	begin
		insert into Kurir(korime, regBr, status, profit, brIsporucenihPaketa) values (@korime, @regBr, 0, 0, 0)
		set @ret = 1
	end
end






